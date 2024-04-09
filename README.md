# ChatTCP

## *Project Members: [Martín Gómez](https://github.com/Electromayonaise), [Julio Prado](https://github.com/jul109), [Daniel Plazas](https://github.com/DanielJPC19)*

Implementar un aplicativo de sala de chat en Java utilizando  sockets TCP. Posee las siguientes funcionalidades: 

1. Crear grupos de chat.

2. Enviar un mensaje de texto a un usuario en especifico o a un grupo.

3. Enviar una nota de voz a un usuario en especifico o a un grupo.

4. Realizar una llamada a un usuario o a un grupo.

5. Se debe guardar el historial de mensajes enviados (texto y audios).

## *Instrucciones de uso:*

### 1. Descargar el proyecto ChatTCP. 
### 2. Compilar los archivos `src/Server/Server.java` y `src/Client/Client.java`
Ingresar a la carpeta `src` y ejecutar los siguientes comandos en la terminal:
```bash


#Primero compilamos el servidor, para ello paremonos en la carpeta Server, primero nos metemos en la carpeta Server con 'cd Server'. Posteriormente ejecutamos el siguiente comando:

javac -d ..\..\out\Server Server.java

#Posteriormente compilamos el cliente, para ello paremonos en la carpeta Client, primero nos salimos de la carpeta Server con 'cd ..\' y luego nos metemos en la carpeta Client con 'cd Client'. Posteriormente ejecutamos el siguiente comando:

javac -d ..\..\out\Client Client.java
```
<div align="center">
  <img  src="https://github.com/Electromayonaise/ChatTCP/blob/main/InstructionAssets/Compile.png"
       alt="compile" /></a>
</div>

### 3. Ejecutar el servidor.

```bash
# Suponiendo que nos encontramos en el directorio del paso anterior, primero debemos de cambiar de directorio ejecutando el siguiente comando:

cd ..\..\out\Server

# Posteriormente ejecutamos el servidor con el siguiente comando:

java Server

# Si todo sale bien, debería de aparecer el siguiente mensaje:

Servidor iniciado, Esperando clientes ... 

# Si aparece el mensaje anterior, el servidor se ha iniciado correctamente, y debes de dejar la terminal abierta para que el servidor siga funcionando.

```
<div align="center">
  <img  src="https://github.com/Electromayonaise/ChatTCP/blob/main/InstructionAssets/Excecute%20server.png"
       alt="excecuteServer" /></a>
</div>

### 4. Ejecutar el cliente.

```bash
# Paremonos en el directorio del archivo compilado del cliente en una nueva terminal, y lo ejecutamos (suponiendo que ya estamos en out\Client):

java Client

# Si todo sale bien, debería de aparecer el siguiente mensaje:

Conectado al servidor
Ingrese su nombre de usuario

# Si aparece el mensaje anterior, el cliente se ha conectado correctamente, y puedes empezar a chatear.
# Si deseas probar las funcionalidades del aplicativo, puedes abrir más terminales y ejecutar el cliente en cada una de ellas. O puedes ejecutar el cliente en otra computadora conectada a la misma red, siempre y cuando se cambie la dirección IP del servidor en lugar de localhost y se actualize por la misma dirección IP en el archivo Client.java.

```

<div align="center">
  <img  src="https://github.com/Electromayonaise/ChatTCP/blob/main/InstructionAssets/Excecute%20client.png"
       alt="excecuteClient" /></a>
</div>

### 5. Instrucciones para conectar clientes en diferentes computadoras.

Para conectar clientes en diferentes computadoras, primero debes de saber la dirección IP de la computadora que está corriendo el servidor. Para ello, en la terminal de la computadora que está corriendo el servidor, ejecuta el siguiente comando: `ipconfig` Busca la dirección IPv4 de la interfaz de red que estás utilizando, y anótala.

<div align="center">
  <img  src="https://github.com/Electromayonaise/ChatTCP/blob/main/InstructionAssets/Select%20server%20IP.png"
       alt="selectServerIP" /></a>
</div>

Posteriormente, en el archivo Client.java, cambia la dirección IP de localhost por la dirección IPv4 que anotaste anteriormente

<div align="center">
  <img  src="https://github.com/Electromayonaise/ChatTCP/blob/main/InstructionAssets/Update%20server%20IP.png"
       alt="updateServerIP" /></a>
</div>

Cambia la misma dirección IP en AudioSocketConnector.java, localizado en la carpeta audio dentro de la carpeta client. 

<div align="center">
  <img  src="https://github.com/Electromayonaise/ChatTCP/blob/main/InstructionAssets/Update%20audioSocketConnector%20IP.png"
       alt="updateServerIP" /></a>
</div>

Por último repite el proceso de compilado y ejecucion del cliente; de igual manera inicia varios clientes para probar las funcionalidades.

### 6. Utilizar las funcionalidades del chat que le aparecen al cliente una vez se ejecuta. 
# 
