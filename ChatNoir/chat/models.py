from django.db import models

# Create your models here.

class Chat(models.Model):
	background_color = models.CharField(max_length=20, null=True)
	text_color = models.CharField(max_length=20, null=True)
	
	def __str__(self):
		return "Chat #" + str(self.id)

class Person(models.Model):
	name = models.CharField(max_length=200)
	username = models.CharField(max_length=70)
	password = models.CharField(max_length=20)
	telephoneNumber = models.CharField(max_length=70)
	bio = models.CharField(max_length=70, null=True)
	address = models.ForeignKey(Chat, on_delete=models.CASCADE)
	
	def __str__(self):
		return self.username

class Group(models.Model):
	name = models.CharField(max_length=200)
	groupname = models.CharField(max_length=70)
	description = models.CharField(max_length=70, null=True)
	grouptype = models.CharField(max_length=1, choices=[('0', 'Public'),('1', 'Private')], default='0')
	address = models.ForeignKey(Chat, on_delete=models.CASCADE)
	creator = models.ForeignKey(Person, on_delete=models.CASCADE, related_name="creator")
	members = models.ManyToManyField(
		Person,
		through='Membership',
		through_fields=('group', 'person'),
	)
	
	def __str__(self):
		return self.groupname

class Membership(models.Model):
	group = models.ForeignKey(Group, on_delete=models.CASCADE)
	person = models.ForeignKey(Person, on_delete=models.CASCADE)
	admin = models.BooleanField()
	
	def __str__(self):
		return self.person.username + " -> " + self.group.groupname

class Message(models.Model):
	content = models.CharField(max_length=1000)
	dispatch = models.DateTimeField('dispatch')
	emitter = models.ForeignKey(Person, on_delete=models.CASCADE)
	receptor = models.ForeignKey(Chat, on_delete=models.CASCADE)
	status = models.CharField(
			max_length=1,
			choices=[('0', 'Rascunho'),('1', 'Enviado'),('2', 'Entregue'),('3', 'Visualizado')],
			default='0'
		)
	
	def __str__(self):
		return "Message #" + str(self.id)
	
class Session(models.Model):
	user = models.ForeignKey(Person, on_delete=models.CASCADE)
	token = models.CharField(max_length=44)

