# TP2
TP2 is a project which intends to convert Toml to Json.

## Info
- TOML Wiki - https://en.wikipedia.org/wiki/TOML
- TOML Github - https://github.com/toml-lang/toml#user-content-comparison-with-other-formats
- TOML to JSON Conversor - https://pseitz.github.io/toml-to-json-online-converter/


- Info about lists in flex
https://www.cs.virginia.edu/~cr4bd/flex-manual/Does-flex-support-recursive-pattern-definitions_003f.html


## Setup

Use [make] to compile the program.

```bash
$ make clean
$ make
```

## Usage

```bash
$ make dir=[pathTofile] run
or
$ toml2Json < [pathTofile] > [destinePath][destiny_filename].json
```

Example:
```bash
make dir="resources/toml/dateTime.toml" run
```

## Test

Runs every .toml file in the resources directory and saves the result and the json resources directory
```bash
$ ./test
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)
