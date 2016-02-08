# ChatParser

This code parses the chat message entered by user for 3 mentioned things:<br/>
1. Mentions: All the mentions i.e. text prefixed with '@' and ends with a non-word character will be considered a mention.<br/>
2. Emoticons: All the text closed inside Parethesis will be consider a emoticon. Such as (happy), (angry) etc. Also the emoticon should not be more than 15 chars.<br/>
3. Links: All the links in the chat message. After finding the link, it also gets the Title for all those links. For this, it uses JSOUP HTML parser to parse the HTML content of that particular link and finds out the "Title" field from it. This requires a working internet connection.<br/>
<br/>
After finding all the mentioned patterns in the message, it displays the findings in the form of JSON. The JSON pattern is:<br/>
CHAT Message : @bob @john (success) such a cool feature; https://twitter.com/jdorfman/status/430511497475670016<br/>
OUTPUT:<br/>
{<br/>
  "mentions": [<br/>
    "bob",<br/>
    "john"<br/>
  ],<br/>
  "emoticons": [<br/>
    "success"<br/>
  ],<br/>
  "links": [<br/>
    {<br/>
      "url": "https://twitter.com/jdorfman/status/430511497475670016",<br/>
      "title": "Twitter / jdorfman: nice @littlebigdetail from ..."<br/>
    }<br/>
  ]<br/>
}
