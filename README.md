# Project 2: Social Network Analysis with MapReduce

This repository includes starter files and a sample directory structure. You are welcome to use it or come up with your own project structure.

Project Specification: https://www.cs.usfca.edu/~mmalensek/cs677/assignments/project-2.html

# Project Report

Your report goes here!

Example Comment:
```
{
  "edited": false,
  "created_utc": "1325376000",
  "controversiality": 0,
  "body": "Isn't this against the first amendment? Doesn't the first amendment give us the right to assemble and protest?",
  "subreddit_id": "t5_2qh3l",
  "parent_id": "t3_nxrpq",
  "author_flair_text": null,
  "id": "c3ctzsj",
  "score": 5,
  "author_flair_css_class": null,
  "gilded": 0,
  "author": "goishin",
  "score_hidden": false,
  "retrieved_on": 1428104610,
  "ups": 5,
  "downs": 0,
  "name": "t1_c3ctzsj",
  "distinguished": null,
  "subreddit": "news",
  "archived": true,
  "link_id": "t3_nxrpq"
}
```

## Warmup

#### [0.25 pt] How many records are in the dataset?
Elapsed Time: 3:32:02
Total number of entries: 2661983402

#### [0.25 pt] How many unique subreddits are there?
Elapsed Time: 3:32:02 (Solved using same map reduce as records)
Number of subreddits: 417834

#### [0.5 pt] What user wrote the most comments in July of 2012? What was the user’s top three most-upvoted comments?
Elapsed Time: 18:51
```
{"user": Aspel, "count": 3552, "comments": [{"upvotes":1,"comment":"Better not go haunting."},{"upvotes":1,"comment":"Better not go haunting."},{"upvotes":1,"comment":"Better not go haunting."}]}
{"user": iam4real, "count": 3746, "comments": [{"upvotes":2,"comment":"Bush senior redefined wetland via radical policy articulated by his VP Dan Quayle:\n\n[ Define wetland"},{"upvotes":2,"comment":"Bush senior redefined wetland via radical policy articulated by his VP Dan Quayle:\n\n[ Define wetland"},{"upvotes":2,"comment":"Bush senior redefined wetland via radical policy articulated by his VP Dan Quayle:\n\n[ Define wetland"}]}
{"user": doa345, "count": 3884, "comments": [{"upvotes":0,"comment":"those beautiful boobs are just begging to be free. thanks for sharing."},{"upvotes":0,"comment":"those beautiful boobs are just begging to be free. thanks for sharing."},{"upvotes":0,"comment":"those beautiful boobs are just begging to be free. thanks for sharing."}]}
{"user": JonAudette, "count": 3944, "comments": [{"upvotes":-1,"comment":"Are you 12? It's fucking *Facebook*....who the Hell cares?"},{"upvotes":-1,"comment":"Are you 12? It's fucking *Facebook*....who the Hell cares?"},{"upvotes":-1,"comment":"Are you 12? It's fucking *Facebook*....who the Hell cares?"}]}
{"user": tweet_poster, "count": 3980, "comments": [{"upvotes":0,"comment":"***jerezim***:\n\n&gt;&amp;#91;2012/07/04&amp;#93;&amp;#91;10:56:25&amp;#93;\n\n&gt;[&amp;#91;Translate&"},{"upvotes":0,"comment":"***jerezim***:\n\n&gt;&amp;#91;2012/07/04&amp;#93;&amp;#91;10:56:25&amp;#93;\n\n&gt;[&amp;#91;Translate&"},{"upvotes":0,"comment":"***jerezim***:\n\n&gt;&amp;#91;2012/07/04&amp;#93;&amp;#91;10:56:25&amp;#93;\n\n&gt;[&amp;#91;Translate&"}]}
{"user": original-finder, "count": 4013, "comments": [{"upvotes":1,"comment":"**Original Submission (100%):** [How I think the world sees us Australians (it's not...)](http://www"},{"upvotes":1,"comment":"**Original Submission (100%):** [How I think the world sees us Australians (it's not...)](http://www"},{"upvotes":1,"comment":"**Original Submission (100%):** [How I think the world sees us Australians (it's not...)](http://www"}]}
{"user": Lots42, "count": 4126, "comments": [{"upvotes":4,"comment":"\"WANNA MAKE OUT?\""},{"upvotes":4,"comment":"\"WANNA MAKE OUT?\""},{"upvotes":4,"comment":"\"WANNA MAKE OUT?\""}]}
{"user": Late_Night_Grumbler, "count": 6941, "comments": [{"upvotes":2,"comment":"I can. Am I some sort of superhero to you?"},{"upvotes":2,"comment":"I can. Am I some sort of superhero to you?"},{"upvotes":2,"comment":"I can. Am I some sort of superhero to you?"}]}
{"user": AutoModerator, "count": 7428, "comments": [{"upvotes":1,"comment":"You are posting a key, and you do not have flair. You need gray flair or better (one or more confirm"},{"upvotes":1,"comment":"You are posting a key, and you do not have flair. You need gray flair or better (one or more confirm"},{"upvotes":1,"comment":"You are posting a key, and you do not have flair. You need gray flair or better (one or more confirm"}]}
{"user": Apostolate, "count": 7978, "comments": [{"upvotes":1,"comment":"Definitely don't need you for that, also you're the least efforted novelty ever."},{"upvotes":1,"comment":"Definitely don't need you for that, also you're the least efforted novelty ever."},{"upvotes":1,"comment":"Definitely don't need you for that, also you're the least efforted novelty ever."}]}
{"user": ModerationLog, "count": 10223, "comments": [{"upvotes":1,"comment":"--- \n\n[&amp;quot;Happy Meal&amp;quot; illegal in Chile.](http://www.reddit.com/r/worldnews/comments/"},{"upvotes":1,"comment":"--- \n\n[&amp;quot;Happy Meal&amp;quot; illegal in Chile.](http://www.reddit.com/r/worldnews/comments/"},{"upvotes":1,"comment":"--- \n\n[&amp;quot;Happy Meal&amp;quot; illegal in Chile.](http://www.reddit.com/r/worldnews/comments/"}]}
{"user": qkme_transcriber, "count": 18098, "comments": [{"upvotes":1,"comment":"Hello! I am a bot who posts transcriptions of Quickmeme links for anybody who might need it.\r\n\r\n&gt;"},{"upvotes":1,"comment":"Hello! I am a bot who posts transcriptions of Quickmeme links for anybody who might need it.\r\n\r\n&gt;"},{"upvotes":1,"comment":"Hello! I am a bot who posts transcriptions of Quickmeme links for anybody who might need it.\r\n\r\n&gt;"}]}
{"user": [deleted], "count": 4219324, "comments": [{"upvotes":2,"comment":"[deleted]"},{"upvotes":2,"comment":"[deleted]"},{"upvotes":2,"comment":"[deleted]"}]}
```
skipped winners:
| user                	| posts   	|
|---------------------	|---------	|
| [deleted]           	| 4219324 	|
| qkme_transcriber    	| 18098   	|
| ModerationLog       	| 10223   	|
| Apostolate          	| 7978    	|
| AutoModerator       	| 7428    	|
| Late_Night_Grumbler 	| 6941    	|
| Lots42              	| 4126    	|
| original-finder     	| 4013    	|
| tweet_poster        	| 3980    	|
#### [1 pt] Choose a day of significance to you (e.g., your birthday), and retrieve a 5% sample of the comments posted on this particular day across all 5 years of the dataset.
#### [1 pt] The number of comments posted per year will likely trend upward over time as more users join Reddit. Use feature scaling to normalize the number of comments per month from 0.0 to 1.0 and plot the values for each year. This way, we can isolate the proportion of comments across months. Do you notice any patterns?


The project specification defines several questions that you will answer with MapReduce jobs. You should edit this document (README.md) with your answers as you find them, including figures, references, etc.