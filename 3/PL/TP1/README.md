# TP1
TP1 is a project which intends to convert html page comments from [publico](https://www.publico.pt/) to json.

## Setup

Use [make] to compile the program.

```bash
$ make clean
$ make
```

Use the following commands to setup the html files.

* Transform one file into UTF-8 encoding.
```bash
$ iconv -f WINDOWS-1252 -t UTF-8 Publico_extraction_portuguese_comments_4.html > [destiny_filename].html
```

Example:
```bash
$ iconv -f WINDOWS-1252 -t UTF-8 Publico_extraction_portuguese_comments_4.html > utf8.html
```

* Transform all files in a certain directory into UTF-8 encoding and saves in given destiny directory
```bash
$ script.sh [sourceDirectory] [destinyDirectory]
```
Example:
```
$ script.sh resources/FilesPublico/ resources/FilesPublicoUTF8/
```

## Usage

```bash
$ make dir=[pathTofile] run
or
$ public2NetLang < [pathTofile] > [destinePath][destiny_filename].json
```

Example:
```bash
make dir="resources/Publico_extraction_portuguese_comments_4.html" run
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)
