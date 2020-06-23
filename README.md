# XML to JSON parsing library

![Generic badge](https://img.shields.io/badge/SemVer-0.1.0-green.svg)]()

There are many libraries out there for this purpose, and this is one more. The problem with most of the methods to convert XML to JSON is that some of the XML structure often get lost in the translation. The other way around, creating XML from a JSON structur, is not that easy either. XML and JSON are very different animals. I have worked with something called "tdstruct" from the "tripledollar" JavaScript project, and it is a JavaScript structure very similar to "JsonMl". This structure is compact and can represent any HTML/XML and serializes to JSON. I made this Java library with this JSON structure as a base. It has simple rules:

1. An element is represented by a JSON array where its first element is the element name as a JSON string.
2. Attributes are represented in a JSON object as key/value pairs.
3. Any other JSON type is interpreded as XML text.

Example of a "tdstruct" in JSON, that can be rendered as XML.

```json
[
    "html",
    {
        "xmlns:xhtml": "http://www.w3.org/1999/xhtml"
    },
    [
        "xhtml:head",
        [
            "title",
            "testing"
        ]
    ],
    [
        "xhtml:body",
        [
            "!--",
            "This is a comment!"
        ],
        [
            "h1",
            {
                "class": "default"
            },
            "Duke was here!"
        ]
    ]
]
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<html xmlns:xhtml="http://www.w3.org/1999/xhtml">
	<xhtml:head>
		<title>testing</title>
	</xhtml:head>
	<xhtml:body>
		<!-- This is a comment! -->
		<h1 class="default">Duke was here!</h1>
	</body>
</html>
```




