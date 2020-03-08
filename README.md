# glosa-disqus-import

Export comments from Disqus (XML format) to a simple JSON.

``` json
[
  {
    "id": 2166957614,
    "parent": 29293782,
    "createdAt": 1352672642,
    "author": "Quijote",
    "thread": "https://programadorwebvalencia.com/clojure-que-es-y-para-que-sirve/",
    "message": "<p>Curioso sí que parece...</p>"
  },{
    "id": 2166957615,
    "parent": 51293771,
    ...
```

Superpowers:

- Transform dates to unix time.
- It unifies the children and the comments.
- Keep the reference to the parents.
- It removes irrelevant data.

## Usage

1) Download `dist/glosa-disqus-import-x.x.x-standlone.jar`.

2) Decompress Disqus export.

3) Runs.

``` bash
java -jar glosa-disqus-import-x.x.x-standlone.jar [file]
```

Example

``` bash
java -jar glosa-disqus-import-x.x.x-standlone.jar myblog-2020-03-04T18%3A36%3A47.800594-all.xml
```

In the same directory you will create the JSON.

## License

Copyright © 2020 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
