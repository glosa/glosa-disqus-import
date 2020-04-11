# Glosa Disqus import

## Export comments from Disqus (XML format) to a simple JSON.

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

## Prerequisites: export the comment XML from Disqus

1) Enter the Disqus Administrative panel.

![step 1](media/1.jpg)

2) Enter to Community.

![step 2](media/2.jpg)

3) Click in Export.

![step 3](media/3.jpg)

You will receive an email with all the compressed comments.

## Usage

1) Make sure you have Java installed.

2) Download the latest version of import (`glosa-disqus-import-{version}-standalone.jar`).

https://github.com/glosa/glosa-disqus-import/releases

3) Decompress Disqus export. You will get an `XML`.

4) Runs.

``` bash
java -jar glosa-disqus-import-x.x.x-standlone.jar [file]
```

Example

``` bash
java -jar glosa-disqus-import-x.x.x-standlone.jar myblog-2020-03-04T18%3A36%3A47.800594-all.xml
```

In the same directory you will create the JSON.

## Create your own JAR

1) Make sure you have **openjdk** or **oracle-jdk** installed, **clojure** and  **leiningen**.

### MacOS

``` sh
brew install openjdk clojure leiningen
```

### Debian/Ubuntu

``` sh
sudo apt install default-jdk clojure leiningen
```

2) Clone the repository and enter the generated folder.

``` sh
git clone https://github.com/glosa/glosa-disqus-import.git
cd glosa-disqus-import
```

3) Run the following command to build a `jar` file.

`lein uberjar`

After this two files should be created in `target/`. We will use the standalone version: `glosa-disqus-import-{version}-standalone.jar`.
