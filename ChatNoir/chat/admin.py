from django.contrib import admin

from .models import Chat, Person, Group, Membership, Message

# Register your models here.

admin.site.register(Chat)
admin.site.register(Person)
admin.site.register(Group)
admin.site.register(Membership)
admin.site.register(Message)
