# Project 2: Social Network Analysis with MapReduce

This repository includes starter files and a sample directory structure. You are welcome to use it or come up with your own project structure.

Project Specification: https://www.cs.usfca.edu/~mmalensek/cs677/assignments/project-2.html

# Project Report

Example Post Json:
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
Elapsed Time: 1:14:01

I took the top commenter that appeared to not be a bot.

Posts from user: Apostolate

| upvotes | post body |
|---------|------|
| 1592 |  Many of my age group, under thirty, have grown up with all media free at their finger tips. Napster, limewire, torrenting, the internet, youtube, we want our content with no charge, with ads, and then we want to block those ads.\n\nThat's why people think this way. |
| 1608 | Love means saying sorry to one person a lot more than anyone else, and the difference is, they probably do forgive you. |
| 1637 | I live in new york, and all I have to say is, **** you. |
| 1863 | &gt;I don't want to be an Eagle Scout if a young man who is gay can't be one, too.\n\nThat's all that needed to be said. Great line. |

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
Elapsed Time: 0:50:06

| type | Number of posts saved |
| ---- | ----- |
| Expected | 364405.7 |
| Actual | 329613 |

Expected = total_posts * 0.05 / 365.25

365.25 is average number of days over a 4 year span per year

#### [1 pt] The number of comments posted per year will likely trend upward over time as more users join Reddit. Use feature scaling to normalize the number of comments per month from 0.0 to 1.0 and plot the values for each year. This way, we can isolate the proportion of comments across months. Do you notice any patterns?
Elapsed Time: ~5:22:00
![Alt text](images/normal_dist.png?raw=true "Distribution")
<!--![alt text](https://github.com/usf-cs677-fa18/P2-mcdomingo/blob/master/images/Normal%20Distribution.png "Distribution")-->

## Analysis Part One

#### [1 pt] Screamers
It is well known that WRITING IN ALL CAPS ONLINE IS A SUBSTITUTE FOR SCREAMING… OR YELLING. *cough!* Write a job to find users that scream a lot, and provide a screamer score (a highly-technical metric expressed as the percentage of uppercase letters used in their comments).
* For future reference (when we really want to get something off our chest), what are the top 5 subreddits for scream-y comments?

Initial thoughts was to take a flat score for each subreddit and count the number of capitol letters and take the values that are the highest. But upon inspection it seems that alot of the subreddits that either had 100% uppercase or 0% uppercase letters also had very few comments. In order balance out and get a more accurate measurement we only included subreddits with at least 50 comments to get a better representation of the true screamiest subreddit. We use the number 50 since I put the cutoff at 50 since that is how much information I believe to get a better sense of how screamy a subreddit is. 

*this tables is generated using data from 2012

| SubReddit               | Screamer Score              |
|-------------------      |----------------             |
| CircLoljerk  | 0.66468 	    |
| gats             | 0.6145077     |
| MinecraftCirclejerk | 0.6032342  |
| counting | .5589686  |
| GhettoJerk | .5549712 |




#### [3 pt] Readability
Write a job that computes Gunning Fog Index and Flesch-Kincaid Readability (both reading ease and grade level) of user comments.
* Choose a subreddit and plot the distribution of these scores using a histogram.
* Find three subreddits of inscrutables, with users that write extremely unreadable comments.


#### [2 pt] Key Terms
Calculate the TF-IDF for a given subreddit.
* Produce a Tag Cloud of the terms (note: this doesn’t have to be integrated into your code; simply including the image is enough).

![Alt text](images/corgi.png?raw=true "r/rarepuppers")

#### Toxicity
Using Sentiment Analysis, determine the top 5 positive subreddits and top 5 negative subreddits based on comment sentiment.
Our original approach to the sentiment analysis was to use the Standford NLP library to generate a score based on average sentiment score and number of sentences. This apporach however was riddled with issue aside from not running due a difference in protobuf versions. 

| Pros                    | Cons         	              |
|-------------------      |----------------             |
| Accuracy is acceptable  | Library is very large 	    |
| Easy to use             | Take a long time to run     |
| Machine learning models | Lots of functions not used  |


As using the library has failed we approached the problem from a different perspective. Instead of using a machine learning model to determine sentiment, we decided to use a word list the consisted of a word and a value, the value being a positive or negative score based on the sentiment. Then we simply added up all the sentiment values to get an absolute value for negativity. An alternative was to use relative scores in order to normalize the data set. However using an absolute value seems to create the most readable results as we should not weigh a small subreddit the same as a large one. This is mostly because using a relative scale would acutally allow the subreddits with less comments to skew the data ( since one positive comment would automatically make them the most positive sub reddit ). 

| Pros                    | Cons         	              |
|-------------------      |------------	                |
| Easy to implement       | Accuracy is questionable    |
| Faster Computations     | Take a long time to run     |
| Flexable word list      | Lots of functions not used  |

### Absolute Sentiment Score (2012)
|Subreddit|Sentiment Score |
|------- |--------------- |
|trees| 10792.0779|
|pics| 9506.3160|
|mylittlepony|4417.8108|
|funny|3667.3823|
|aww| 358|3584.1785|
|...|....|
|news|-324.1795|
|MensRights|-440.3776|
|4chan|-944.4091|
|politics|-1668.8645|
|worldnews|-1892.4820|

Interesting things about the anaylsis:
Even the most negative subreddit has a lower absolute value than the 5th postive subreddit. Upon further inspection it takes 15 subreddits to find a positive subreddit with the same absolute value as the most negative one. Of course this can be for a variety of reasons. I suspect it is because the method we used do not account for words that modify the meaning of other words. For example "I am very happy" is a postive sentence, but "I am NOT happy" is a negative one. While this can be also true for the reverse "I am not angry", I suspect that these sentences are not as common.

  It is ressuring that the subreddits listed seem to correctate to the topic. It does make sence that politics and news would have on average a negative sentiment score as those topics are highly polarized. While both trees and MensRights are clearly satirical (I hope), it is obvious that trees would have less offensive or negative content than MensRights. While I believe the sentiment of MensRights is most likely negative (but in a joking way?).  
  For the positive subreddits, I expect that those who comment on my little pony are probably fans of the show and would comment positive things about it. This also applies to funny and aw which also seem like subreddits that would not have upsetting content in contrast to somthing that can be potentially offensive. 

## Analysis Part 2


#### [3 pt] Backstory
Given a specific user, find out more about them: where they’re from, what things they like/dislike, and other data about their background (think of at least 2 more things to determine). Note that this should be automated; I should be able to give you a username and you’ll produce a backstory for them. Provide a three sample user backstories in your report (you can clean these up when you add them to the report – they don’t have to be raw comments).

The Backstory generator will not nessessary produce a backstory instant instead it will produce a range of metrics that can be reused in future jobs. Instead for this question we will simply be analysising these metrics to produce a backstory for a use. Since there are not many backstories to analysis in this particualar case (we are only analysis three) We can do this manueal but in the future if we were to implement this on a larger scale a simple script can also achieve the same goal. Below we will include both the human analyed backstory and the script one ( the human one being much more detailed ).

|Trait | Description| Implementation summary|
|------|------------|-----------------------|
|Location | Where this user is located| Tracks certain phrases to deduce location
|Number of Comments| The number of comments a user posts | Tracks the number of comments a users postes over time
|Likes | What the users interests are | We can assume that if a user comments on a subreddit it is something they have an interest in
|Education Level| A guess on the level of education of a user| We can use the readabilty of a users comments to make a guess on education level
|Temperment| The Users overall temperment, | Uses Sentiment analysis to determine if the person is a overall a positive or negative person
|Agreeability | See how well a user gets along with other people | Using upvotes we can see how agreeable a user is. More upvotes means a higher agreeablity score.

*a note for the dislikes: It is very difficult to find out what a user dislikes based on their reddit activity (the subreddits that users visit). This is because for the vast majority of people will not be visiting reddits that they have no interested in. An alterative way to actually track the dislikes of a user may require some more natural language processing to get dislikes based on comments and sentiment.





#### [2 pt] A day in the life
You are a struggling scriptwriter trying to make it big in Hollywood. Find an interesting user with your backstory job, then trace their commenting activities across the site over time. Use this combination of data to build a story about the user’s life: what they do on a regular basis, who their friends are, their hopes/dreams, etc. You have some creative license here.
#### [2 pt] Matchmaker
While you work on your hit movie script, you need to pay the bills. Use your analysis skills to match up users with similar interests so that they can find love or friendship. If your algorithm is effective, you might just be able to pay rent this month!
          Note: remember to explain your methodology in your report.
#### [2 pt] Music Recommendations
After graduating from USF, you found a startup company that aims to provide personalized music recommendations using big data analysis. In other words, the pitch is that users can “just be themselves” on social media and the service will determine their personality to provide new music recommendations. Design a MapReduce job to do this.
Note: remember to explain your methodology in your report.
#### [4 pt] Design Two
 Make your own Now that you’ve found the answers to the questions above, design two of your own questions to answer. These should be sufficiently difficult, and you should be creative! You should start with a question, and then propose a predicted answer or hypothesis before writing a MapReduce job to answer it. If you come up with a particularly challenging question, it can count for two (ask first).

Some ideas:
* Visualization of related features. Your visualization should help tell a story.
* Clustering related users, comments, or subreddits.
* Summary statistics: finding mins, maxes, standard deviations, or even correlations between variables to tell us something about a subreddit or multiple subreddits. For example, perhaps users that visit /r/technology also frequently visit /r/android.
* Friend graph: can you link together ‘related’ users based on some shared interest? Maybe several users visit the same collection of subreddits. The PageRank could come in handy here.

##### One: Use a bloom filter to measure how similar a user's vocabulary is to another user's

##### Two: Five most diverse/most important subbreddits per month

## Wrap Up
#### [1 pt] Project retrospective
