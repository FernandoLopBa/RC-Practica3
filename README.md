# Práctica HTTP

## Requisitos

1. Instalar [Leiningen](https://leiningen.org/)
2. Instalar NPM

## Ejecución

Para levantar el servidor, abrir una terminal en este proyecto y ejecutar

```shell
$ lein run
```

Esto también permite conectarse a un REPL remoto al puerto 7000 de localhost.

Para levantar el front-end, en una terminal distinta, ejecutar

```shell
$ npx shadow-cljs watch app
```

Que levantará un entorno de desarrollo con hot-reload y cada que se salve un archivo, se
actualizará la UI.

## Ejercicios

Los namespaces relevantes a modifcar son `practica-html.view` para front-end y `practica-html.routes.home` 
para back-end, correspondientes a los archivos `src/cljs/practica_html/view.cljs` y 
`src/clj/practica_html/routes/home.clj` respectivamente.

### Servicios web

Para cada servicio web se implementará una interfaz de usuario que muestre el resultado de
la consulta al back-end y deben ser capaces de responder al menos un código de error de HTTP apropiado,
para saber qué código se debe responder en cada situación se puede usar [este listado](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/100),
aunque será necesario que se investigue de más fuentes en Internet.

La lista de end-points a implementar es la siguiente:

1. End-point `"/suma"` que dado dos números los sume y devuelva el resultado. Si algún valor 
   no es un número debe responder un código de error 400 (Bad Request).
2. End-point `"/suma-general"` que dada un vector de números los sume y devuelva el resultado.
3. End-point `"/calculadora"` que reciba un mapa con las llaves `:valor1`, `:valor2` y `:op`
   y devuelve el resultado de aplicar `:op` a `:valor-1` y `:valor2`.
4. End-point `"/log-in"` que recibe un usuario y una contraseña, y responde `{:session :ok}`
   únicamente si las credenciales están en un conjunto predefinido de credenciales en el end-point 
   (pueden ser las que el equipo guste).
5. End-point `"/secure-password"` que reciba una cadena y responda si es una contraseña 
   segura de acuerdo a criterios que investigues por Internet (se debe anexar la fuente en 
   un comentario en el end-point), y en caso de no serlo, debe emitir alguna recomendación 
   para logar que lo sea.
6. End-point `"update-value"` que define un mapa constante en el back-end (por ejemplo, 
   `{:a 1, :b 2}`), recibe una llave existente en el mapa junto con un valor nuevo y 
   devuelve un nuevo mapa con el valor actualizado.
7. End-point `"/integrantes-de-equipo"` que devuelve una lista que contiene a los integrantes
   del equipo junto con un enlace a imagen asociada a cada uno.
8. Implementar un botón que al hacer click permita ver la fotografía de al menos 5 instalaciones
   de la Facultad de Ciencias (por ejemplo, el taller de [Sistemas operativos](https://www.fciencias.unam.mx/sites/default/files/Imgs/Labs%20y%20talleres/aulas-computo/SO.jpg))
   usando la página de la facultad como fuente: https://www.fciencias.unam.mx/vida-en-ciencias/instalaciones.
9. End-point que sea invocado con el método `PATCH`.
10. End-point que sea invocado con el método `PUT`.
11. End-point que sea invocado con el método `DELETE`.


## Flujo recomendado de desarrollo

Para utilizar las ventajas de la programación dinámica en Clojure se recomienda:
1. Levantar el entorno de desarrollo en el cliente (shadow-cljs)
2. Levantar el servidor (usando leiningen)
3. Conectar un REPL al back-end en el puerto 7000
4. Al hacer una modificación en el código del servidor, cambiarse al namespace `practica-html.routes.home` y recompilar el archivo

De esta manera se podrán probar las modificaciones en el back-end muy rápido, de lo contrario
habrá que detener el back-end (con `ctrl + c`) y volver a levantarlo (`lein run`).

