## Desargar NsignPTApp

Para utilizar este proyecto debemos importarlo primero a nuestro IDE por lo que clonaremos el repositorio. Una vez descargado lo importamos y compilamos para empezar a usar la App.

## A tener en cuenta

Estamos usando valores estáticos para la URL y el nombre del archivo a utilizar. Para utilizar otros hay que modificar estas constantes ubicadas en la carpeta **common** y en el archivo **Constants**.

```
public static String URL_ZIP = 'https://media.nsign.tv/media/NSIGN_Prueba_Android.zip';`
public static final String EVENTS_JSON = "events.json";
```
**URL_ZIP** es la URL de donde descargara el archivo ".zip".
**EVENTS_JSON** es el nombre del archivo JSON que se desea descargar.

## Consideraciones

- La App tiene un minSdk de 24 (Android 7.0)
- Se ha centrado el display para mostrar el contenido en un recuadro negro.
- La zona de reproducción estará marcada por un borde rojo.
- Se han centrado las imágenes dentro de la zona de reproducción en caso de que no ocupen todo el display.
- Se han añadido mensajes de error en diversos puntos para una mejor experiencia de usuario.
- Se han añadido botones para reiniciar el proceso en caso de necesitarlos.
