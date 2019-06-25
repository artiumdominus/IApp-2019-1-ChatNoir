# chat/api.py
from django.conf.urls import url

from restless.dj import DjangoResource
from restless.preparers import FieldsPreparer, CollectionSubPreparer
from restless.resources import skip_prepare

from .models import Chat, Person, Group, Membership, Message, Session

from datetime import datetime

import secrets

class ChatResource(DjangoResource):
	preparer = FieldsPreparer(fields={
			'id': 'id',
			'background_color': 'background_color',
			'text_color': 'text_color',
		})

	def is_authenticated(self):
		# Open everything wide!
		# DANGEROUS, DO NOT DO IN PRODUCTION.
		return False;
	
		# Alternatively, if the user is logged into the site...
		# return self.request.user.is_authenticated()
		
		# Alternatively, you could check an API key. (Need a model for this...)
		# from myapp.models import ApiKey
		# try:
		#     key = ApiKey.objects.get(key=self.request.GET.get('api_key'))
		#         return True
		# except ApiKey.DoesNotExist:
		#     return False

	# GET /api/chats/
	def list(self):
		return Chat.objects.all()

	# GET /api/chats/<pk>/
	def detail(self, pk):
		return Chat.objects.get(id=pk)

	# POST /api/chats/
	def create(self):
		return Chat.objects.create(
				background_color = self.data['background_color'],
				text_color = self.data['text_color']
			)

	# PUT /api/chats/<pk>/
	def update(self, pk):
		try:
			chat = Chat.objects.get(id=pk)
		except Chat.DoesNotExist:
			chat = Chat()

		chat.background_color = self.data['background_color']
		chat.text_color = self.data['text_color']
		chat.save()
		
		return chat

	# DELETE /api/chats/<pk>/
	def delete(self, pk):
		Chat.objects.get(id=pk).delete()

	# Experimental : Custom endpoint
	def __init__(self, *args, **kwargs):
		super(ChatResource, self).__init__(*args, **kwargs)
		self.http_methods.update({
				'schema' : {
					'GET': 'schema',
				}
			})

	@skip_prepare
	def schema(self):
		return {
			'fields' : {
				'id': 'integer',
				'background_color': 'string',
				'text_color': 'string'
			}
		}

	@classmethod
	def urls(cls, name_prefix=None):
		urlpatterns = super(ChatResource, cls).urls(name_prefix)
		return [
			url(r'^schema/$', cls.as_view('schema'), name=cls.build_url_name('schema', name_prefix)),
		] + urlpatterns

class PersonResource(DjangoResource):
	preparer = FieldsPreparer(fields={
			'id': 'id',
			'name': 'name',
			'username': 'username',
			'telephoneNumber': 'telephoneNumber',
			'bio': 'bio',
			'address': 'address.id',
		})

	def is_authenticated(self):
		if self.endpoint in ('list', 'detail', 'create'):
			return True
		else:
			try:
				self.session = Session.objects.get(token=self.request.META.get('HTTP_TOKEN'))
				return True
			except:
				return False

	# GET /api/persons/
	def list(self):
		return Person.objects.all()

	# GET /api/persons/<pk>/
	def detail(self, pk):
		return Person.objects.get(id=pk)

	# POST /api/persons/
	def create(self):
		return Person.objects.create(
				name = self.data['name'],
				username = self.data['username'],
				password = self.data['password'],
				telephoneNumber = self.data['telephoneNumber'],
				bio = self.data['bio'],
				address = Chat.objects.create()
			)

	# PUT /api/persons/<pk>/
	def update(self, pk):
		try:
			session = Session.objects.get(token=self.request.META.get('HTTP_TOKEN'))
			if str(session.user.id) == pk:
				person = session.user
				person.name = self.data['name']
				person.username = self.data['username']
				person.password = self.data['password']
				person.telephoneNumber = self.data['telephoneNumber']
				person.bio = self.data['bio']
				person.save()
				
				return person
			else:
				raise Warning('You just can update your own account' + '\n' +
					'You: ' + str(session.user.id) + ' Target: ' + str(pk))
		except:
			raise Warning('Wrong token') # Este try-except pode dar bug ao tentar lançar o Warning interno.

	# DELETE /api/persons/<pk>/
	def delete(self, pk):
		session = Session.objects.get(token=self.request.META.get('HTTP_TOKEN'))
		if str(session.user.id) == pk:
			person = Person.objects.get(id=pk)
			person.address.delete()
		else:
			raise Warning('You just can delete your own account')
		
	def is_debug(self):
		return False

class GroupResource(DjangoResource):
	member_preparer = FieldsPreparer(fields={
			'id': 'id',
			'username': 'username',
		})

	preparer = FieldsPreparer(fields={
			'id': 'id',
			'name': 'name',
			'groupname': 'groupname',
			'description': 'description',
			'type': 'grouptype',
			'address': 'address.id',
			'creator': 'creator.username',
			'members': CollectionSubPreparer('members.all', member_preparer),
		})

	def is_authenticated(self):
		try:
			session = Session.objects.get(token=self.request.META.get('HTTP_TOKEN'))
			return True
		except:
			return False

	# GET /api/groups/
	def list(self):
		return Group.objects.filter(grouptype='0') # Adicionar grupos que o usuário logado participa
	
	# GET /api/groups/<pk>/
	def detail(self, pk):
		group = Group.objects.get(id=pk)
		if group.grouptype == '0':
			return group
		else:
			raise Warning('The group you are trying to access is private')

	# POST /api/groups/
	def create(self):
		session = Session.objects.get(token=self.request.META.get('HTTP_TOKEN'))
		group = Group.objects.create(
			name = self.data['name'],
			groupname = self.data['groupname'],
			description = self.data['description'],
			grouptype = self.data['type'],
			address = Chat.objects.create(),
			creator = session.user
		)
		
		Membership.objects.create(
			group = group,
			person = session.user,
			admin = True
		)
		
		return group

	# PUT /api/groups/<pk>/
	def update(self, pk):
		session = Session.objects.get(token=self.request.META.get('HTTP_TOKEN'))
		try:
			group = Group.objects.get(id=pk)
		except:
			raise Warning('The group you are trying to update does not exist')
		
		if session.user == group.creator:
			group.name = self.data['name']
			group.groupname = self.data['groupname']
			group.description = self.data['description']
			group.grouptype = self.data['type']
			group.save()
			
			return group
		else:
			raise Warning('You have no permission to update this group')

	# DELETE /api/groups/<pk>/
	def delete(self, pk):
		session = Session.objects.get(token=self.request.META.get('HTTP_TOKEN'))
		try:
			group = Group.objects.get(id=pk)
		except:
			raise Warning('The group you are trying to delete does not exist')

		if session.user == group.creator:
			group.address.delete()

			return group
		else:
			raise Warning('You have no permission to delete this group')

	def is_debug(self):
		return False

class MembershipResource(DjangoResource):
	preparer = FieldsPreparer(fields={
			'id': 'id',
			'group': 'group.groupname',
			'person': 'person.username',
			'admin': 'admin', 
		})

	def is_authenticated(self):
		try:
			self.session = Session.objects.get(token=self.request.META.get('HTTP_TOKEN'))
			return True
		except:
			return False

	# GET /api/memberships/
	def list(self):
		return Membership.objects.all()

	# GET /api/memberships/<pk>/
	def detail(self, pk):
		return Membership.objects.get(id=pk)

	# POST /api/memberships/
	def create(self):
		group = Group.objects.get(id=self.data['group'])
		person = Person.objects.get(id=self.data['person'])
		
		if person == self.session.user:
			if group.grouptype != '0':
				raise Warning('You cannot join a private group')
		else:
			if group.creator != self.session.user:
				raise Warning('You cannot add a person in a group that is not yours')
		
		return Membership.objects.create(
				group = group,
				person = person,
				admin = False
			)

	# PUT /api/memberships/<pk>/
	def update(self, pk):
		membership = Membership.objects.get(id=pk)
		
		if membership.group.creator != self.session.user:
			raise Warning('You cannot add/remove admins in a group that is not yours')
		
		membership.admin = self.data['admin']
		membership.save()
		
		return membership

	# DELETE /api/memberships/<pk>/
	def delete(self, pk):
		membership = Membership.objects.get(id=pk)
		
		if membership.group.creator != self.session.user or not self.is_admin(self.session.user, membership.group):
			raise Warning('You cannot remove members from a group that is not yours or you don\'t administrate')
		
		membership.delete()
	
	def is_debug(self):
		return False
	
	@staticmethod
	def is_admin(person, group):
		try:
			membership = Membership.objects.get(person=person, group=group)
			return membership.admin
		except:
			return False

class MessageResource(DjangoResource):
	preparer = FieldsPreparer(fields={
			'id': 'id',
			'content': 'content',
			'dispatch': 'dispatch',
			'emitter': 'emitter.username',
			'receptor': 'receptor.id',
			'status': 'status',
		})

	def is_authenticated(self):
		try:
			self.session = Session.objects.get(token=self.request.META.get('HTTP_TOKEN'))
			return True
		except:
			return False

	# GET /api/messages/
	def list(self):
		return Message.objects.all()

	# GET /api/messages/<pk>/
	def detail(self, pk):
		return Message.objects.get(id=pk)

	# POST /api/messages/
	def create(self):
		return Message.objects.create(
				content = self.data['content'],
				dispatch = datetime.now(),
				emitter = self.session.user,
				receptor = Chat.objects.get(id=self.data['receptor']),
				status = '1'
			)

	# PUT /api/messages/<pk>/
	def update(self, pk):
		message = Message.objects.get(id=pk)
		message.content = self.data['content']
		message.status = self.data['status']
		message.save()
		
		return message

	# DELETE /api/messages/<pk>/
	def delete(self, pk):
		Message.objects.get(id=pk).delete()
		
	def is_debug(self):
		return False

class Log(DjangoResource):
	
	def is_authenticated(self):
		return True
	
	# POST /api/log/
	def create(self):
		username = self.data['username']
		password = self.data['password']
		
		person = Person.objects.get(username=username)
		
		if person.password == password:
			token = secrets.token_urlsafe()
			Session.objects.create(user = person, token = token)
			return {'token':token}
		else:
			return {'error':'Wrong password'}
		
	# PUT /api/log/x/
	def update(self, pk):
		username = self.data['username']
		password = self.data['password']
		oldtoken = self.request.META.get('HTTP_TOKEN')
		
		person = Person.objects.get(username=username)
		
		if person.password == password:
			session = Session.objects.get(user=person, token=oldtoken)
			newtoken = secrets.token_urlsafe()
			session.token = newtoken
			session.save()
			return {'oldtoken': oldtoken, 'newtoken': newtoken}
		else:
			return {'error':'Wrong password'}
	
	# DELETE /api/log/x/
	def delete(self, pk):
		token = self.request.META.get('HTTP_TOKEN')
		
		session = Session.objects.get(token=token)
		user = session.user
		session.delete()
		
		return {'username': user.username, 'deletedtoken': token}
	
	def is_debug(self):
		return False
