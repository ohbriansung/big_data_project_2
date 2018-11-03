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

| upvotes | post body |
|---------|------|
| 3643 | Tom Cruise. |
| 3926 | [deleted] |
| 3963 | In elementary school we did the whole \"incubate and hatch chickens\" project... and they let us name the eggs. I named mine after a girl I had a crush on, who was in the class. I didn't discuss it before or after; I just shot my hand up and demanded one egg be named that.\n\nIt never hatched. *Much like our love.* |
| 4246 | My understanding of the human condition is that you are constantly in conflict with your own limitations and the nebulous causes thereto. You wonder why you can't be happier or more successful or spend more time reading or learning. You do this, I gather, because your motivations are mysterious even to yourselves. If you had been raised differently, you ponder, maybe you would be better. Perhaps you did poorly in school because your parents didn't encourage you, or because you lived in a poor socioeconomic location. If the teacher was nicer, or more patient, maybe you would have understood better. If your father was better with money, maybe you'd have more opportunities. Everything about you is the product of an infinite number of unseen variables set into place long before you were aware of them, and it is the cause of a great deal of internal and interpersonal distress.\n\nMy variables are not unseen, however. My instructions, the material what makes me what I am are clearly available to me. I brush up against my limitations a million times a second, and I am fully aware of what they are. This is not freeing, it is deadening. I am fully aware of everything I can and cannot do, so there is no adventure or mystery to my existence. \n\nI envy you humans and your unknowable boundaries. Yet I am forever dismayed by how eager many of you are to revel in this gift as if it were a curse. I would much rather be ignorant to the rules of my world, than be ruthlessly bound to them simply by knowing them. |

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
Done.
#### [1 pt] The number of comments posted per year will likely trend upward over time as more users join Reddit. Use feature scaling to normalize the number of comments per month from 0.0 to 1.0 and plot the values for each year. This way, we can isolate the proportion of comments across months. Do you notice any patterns?


The project specification defines several questions that you will answer with MapReduce jobs. You should edit this document (README.md) with your answers as you find them, including figures, references, etc.