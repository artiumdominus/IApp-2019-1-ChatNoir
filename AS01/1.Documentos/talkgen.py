import random

text = open("mesasges.txt","r").read().split("\n")

senders = ["artiumdominus", "ripley", "kylereese", "connorsarah", "connorjohn", "oldbladerunner", "iamthebusiness", "snake", "mirrorshades", "major"]

addresses = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c"]

for i in range(0,len(text)-1,2):
	print("INSERT INTO Mensagem (conteudo, envio, status, emissor, receptor) VALUES (\"" + text[i] +
	   " " + text[i+1] + "\", \"2019-04-27 13:06:00\", \"enviado\", \"" + random.choice(senders) +
	   "\", \"" + random.choice(addresses) + "\");")
