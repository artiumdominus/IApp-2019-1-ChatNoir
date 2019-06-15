# chat/api.py
from django.conf.urls import url

from restless.dj import DjangoResource
from restless.preparers import FieldsPreparer, CollectionSubPreparer
from restless.resources import skip_prepare

from .models import Chat, Person, Group, Membership, Message

from datetime import datetime

class ChatResource(DjangoResource):
	preparer = FieldsPreparer(fields={
			'id': 'id',
			'background_color': 'background_color',
			'text_color': 'text_color',
		})

	def is_authenticated(self):
		# Open everything wide!
		# DANGEROUS, DO NOT DO IN PRODUCTION.
		return True;
	
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

		chat.background_color = self.data['background_color'],
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
		return False;

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
			person = Person.objects.get(id=pk)
		except:
			person = Person(address=Chat.objects.create())

		person.name = self.data['name']
		person.username = self.data['username']
		person.password = self.data['password']
		person.telephoneNumber = self.data['telephoneNumber']
		person.bio = self.data['bio']
		person.save()
		
		return person

	# DELETE /api/persons/<pk>/
	def delete(self, pk):
		person = Person.objects.get(id=pk)
		person.address.delete()

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
		return False

	# GET /api/groups/
	def list(self):
		return Group.objects.all()
	
	# GET /api/groups/<pk>/
	def detail(self, pk):
		return Group.objects.get(id=pk)

	# POST /api/groups/
	def create(self):
		return Group.objects.create(
				name = self.data['name'],
				groupname = self.data['groupname'],
				description = self.data['description'],
				grouptype = self.data['grouptype'],
				address = Chat.objects.create(),
				creator = Person.objects.get(id=1) # Corrigir
			)

	# PUT /api/groups/<pk>/
	def update(self, pk):
		try:
			group = Group.objects.get(id=pk)
		except:
			group = Group(address=Chat.objects.create())
			
			group.name = self.data['name']
			group.groupname = self.data['groupname']
			group.description = self.data['description']
			group.grouptype = self.data['grouptype']
			group.creator = Person.objects.get(id=1) # Corrigir
			group.save()
			
			return group

	# DELETE /api/groups/<pk>/
	def delete(self, pk):
		Group.objects.get(id=pk).delete()
	
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
		return True

	# GET /api/memberships/
	def list(self):
		return Membership.objects.all()

	# GET /api/memberships/<pk>/
	def detail(self, pk):
		return Membership.objects.get(id=pk)

	# POST /api/memberships/
	def create(self):
		Membership.objects.create(
				group = Group.objects.get(id=self.data['group']),
				person = Group.objects.get(id=self.data['person']),
				admin = False
			)

	# PUT /api/memberships/<pk>/
	def update(self, pk):
		try:
			membership = Membership.objects.get(id=pk)
		except:
			membership = Membership()

		membership.admin = self.data['admin']
		
		return membership

	# DELETE /api/memberships/<pk>/
	def delete(self, pk):
		Membership.objects.get(id=pk).delete()

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
		return True

	# GET /api/messages/
	def list(self):
		return Message.objects.all()

	# GET /api/messages/<pk>/
	def detail(self, pk):
		return Message.objects.get(id=pk)

	# POST /api/messages/
	def create(self):
		Message.objects.create(
				content = self.data['content'],
				dispatch = datetime.now(),
				emitter = Person.objects.get(id=1), # Corrigir
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
