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

#### How many records are in the dataset?
Elapsed Time: 3:32:02

Total number of entries: 2661983402

#### How many unique subreddits are there?
Elapsed Time: 3:32:02 (Solved using same map reduce as records)

Number of subreddits: 417834

#### What user wrote the most comments in July of 2012? What was the user’s top three most-upvoted comments?
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

#### Choose a day of significance to you (e.g., your birthday), and retrieve a 5% sample of the comments posted on this particular day across all 5 years of the dataset.
Elapsed Time: 0:50:06

| type | Number of posts saved |
| ---- | ----- |
| Expected | 364405.7 |
| Actual | 329613 |

Expected = total_posts * 0.05 / 365.25

365.25 is average number of days over a 4 year span per year

#### The number of comments posted per year will likely trend upward over time as more users join Reddit. Use feature scaling to normalize the number of comments per month from 0.0 to 1.0 and plot the values for each year. This way, we can isolate the proportion of comments across months. Do you notice any patterns?
Elapsed Time: ~5:22:00
![Alt text](images/normal_dist.png?raw=true "Distribution")
<!--![alt text](https://github.com/usf-cs677-fa18/P2-mcdomingo/blob/master/images/Normal%20Distribution.png "Distribution")-->

#### Analysis Part One

|General Observations ||
|--------|----------------------|
|Users and subreddits with low comments tend to skew the data| To fix this we simply add a minimum number of comments required to actually be included into the results|
|Averages vs Absolute values | Averages are better for when we need equal representation between larger and smaller data sets otherwise we will use absolute values|
|Machine Learning vs Human Analysis| Overall for this project adding machine learning models add significant overhead to the data collection and for now we have decided to use human analysis replace the models in exchange for a performance increase| 

#### Screamers
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




####  Readability
Write a job that computes Gunning Fog Index and Flesch-Kincaid Readability (both reading ease and grade level) of user comments.
* Choose a subreddit and plot the distribution of these scores using a histogram.
* Find three subreddits of inscrutables, with users that write extremely unreadable comments.
##### Limitation
On the internet many comments and typing style can lead to inconsistent results when using the Flesch-Kincaid Algorithm. Internet slang such as "lol" or "rotfl" can result in null values for this algorithm as well as comments being on average shorter than the posts themselves. Because of the lengh of the comments readablity can somtimes not be accuratly judged. In addition mispelled words or gibberish often get an extremely low readabilty score with is not useful for analysis and very very short comments also have a extremely high readability score also skewing the data (however these can be filtered out).


####  Key Terms
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
| Machine learning models | Lots of functions included but not used  |


As using the library has failed we approached the problem from a different perspective. Instead of using a machine learning model to determine sentiment, we decided to use a word list the consisted of a word and a value, the value being a positive or negative score based on the sentiment. Then we simply added up all the sentiment values to get an absolute value for negativity. An alternative was to use relative scores in order to normalize the data set. However using an absolute value seems to create the most readable results as we should not weigh a small subreddit the same as a large one. This is mostly because using a relative scale would acutally allow the subreddits with less comments to skew the data ( since one positive comment would automatically make them the most positive sub reddit ). 

*Both these approaches fall short when looking at context of the comments. While you can see the sentiment of the sentence as a whole you cannot see the context of the comment which at times is very important to determining if a sentence is truly negative or not. Also both versions of sentiment analysis cannot detect satire very well possibly give "dark humour" a negative sentiment score when in reality it may not be the case. 

| Pros                    | Cons         	              |
|-------------------      |------------	                |
| Easy to implement       | Accuracy is questionable    |
| Faster Computations     | Wordlist is much smaller     |
| Flexable word list      | Does Not handle Many Edge cases |

### Absolute Sentiment Score (2012, 5% sample)
|Pos Subreddit|Sentiment Score |Neg Subreddit | Sentiment Score|
|------- |--------------- |--------|-----------------|
|trees| 10792.0779|news|-324.1795|
|pics| 9506.3160|MensRights|-440.3776|
|mylittlepony|4417.8108|4chan|-944.4091|
|funny|3667.3823|politics|-1668.8645|
|aww|3584.1785|worldnews|-1892.4820|

Interesting things about the anaylsis:
Even the most negative subreddit has a lower absolute value than the 5th postive subreddit. Upon further inspection it takes 15 subreddits to find a positive subreddit with the same absolute value as the most negative one. Of course this can be for a variety of reasons. I suspect it is because the method we used do not account for words that modify the meaning of other words. For example "I am very happy" is a postive sentence, but "I am NOT happy" is a negative one. While this can be also true for the reverse "I am not angry", I suspect that these sentences are not as common.

  It is ressuring that the subreddits listed seem to correctate to the topic. It does make sence that politics and news would have on average a negative sentiment score as those topics are highly polarized. While both trees and MensRights are clearly satirical (I hope), it is obvious that trees would have less offensive or negative content than MensRights. While I believe the sentiment of MensRights is most likely negative (but in a joking way?).  
  For the positive subreddits, I expect that those who comment on my little pony are probably fans of the show and would comment positive things about it. This also applies to funny and aw which also seem like subreddits that would not have upsetting content in contrast to somthing that can be potentially offensive. 

# Analysis Part 2


## Backstory

  The Backstory generator will not nessessary produce a backstory instance, but instead it will produce a range of metrics that can be reused in future jobs. Instead for this question we will simply be constructing a story these metrics to produce a backstory for a use. Since there are not many backstories to analysis in this particualar case (we are only analysis three) We can do this by hand but in the future if we were to implement this on a larger scale to get backstories for more users, we would use a machine learning model to generate a back story. While a script could also work to find a backstory, it would not be very detailed since much of a users personality and traits are actually found my looking into the subreddits that they visit rather than the raw metrics we have defined below.. Below we have inclulded a human analyed backstory with detailed analysis and methodologies.

|Trait | Description| Implementation summary|
|------|------------|-----------------------|
|Temperment| The Users overall temperment, | Uses Sentiment analysis to determine if the person is a overall a positive or negative person
|Readabilty Score| A guess on the level of education of a user| We can use the readabilty of a users comments to make a guess on education level
|Number of Comments| The number of comments a user posts | Tracks the number of comments a users postes over time
|Agreeability | See how well a user gets along with other people | Using average upvotes we can see how agreeable a user is. More upvotes means a higher agreeablity score.
|Location | Where this user is located| Tracks certain phrases to deduce location
|Likes | What the users interests are | We can assume that if a user comments on a subreddit it is something they have an interest in

*a note for the dislikes: It is very difficult to find out what a user dislikes based on their reddit activity (the subreddits that users visit). This is because for the vast majority of people will not be visiting reddits that they have no interested in. An alterative way to actually track the dislikes of a user may require some more natural language processing to get dislikes based on comments and sentiment.

*We will structure the analysis as follows, First part of the user analysis can be done with a script, Second part will be analysis personality based on subreddit activity.


## MikeMarx153

|Trait| Value|
|-------|-----------------------|
|User|MikeMarx153|
|Toxic Score| 0.007|
|Readabilty Score | 62.72|
|Comment Count | 52|
|Upvotes | 5.2|
|Location | null|
|Liked Subreddits | loseit:18, books:4, science:4, adviceanimals:4, fatlogic:3, funny:3, nutrition:2, fitness:2, politics:1, asianamerican:1|
 
### What we know about MikeMarx153:
  For the table above we can see that this user is quite an agreeable person overall. His neutral toxicity score indicates that he must not be a particular negative person and seems to post either both postive and negative comments or most flat comment on the posts. In addition he seems to be a user that does not comment on post alot but does on average get a 5 upvotes per post making him have a total of 260 upvotes. This means he likely does not post unless information and using has somthing constructive to bring to a conversation.

  From his likes we can deduct that he may be an slightly overwieght asain-american around high school. His most visited sub reddit "lose it" is primariy a fat lose subreddit and this in combiniation 3 other fitness related subreddits shows me that he is trying to lose weight as about half of his comments are related to fitness. In addition he seems enjoy books and science and probably has a pet (most likely a dog). This user seems to have enough common sense to not post his location on the internet but based on his race we can see that he is likely either from california or texas. While he this does show his top ten subreddits he also goes to 10 other subreddits that are not accounted for that can expand on his backstory.



## IlikeHistory

|Trait| Value|
|-------|-----------------------|
|User|IlikeHistory|
|Toxic Score| 0.0037|
|Readabilty Score | 36.814|
|Comment Count | 178|
|Upvotes | 20.63|
|Location | null|
|Liked Subreddits | atheism:55, AskReddit:42, todayilearned:36, AskHistorians:20, history:6, bestof:6, IAmA"4, movies:3, books:3, TrueReddit:1|

### What we know about IlikeHistory

  One of the first things that stand out for this user is that his name correlates with his interest alot. From his sentiment score and education level in combintion with his interest, it seems that much of the comments are factual by nature rather than opinionative lacking words that can be thought of as postive or negative. This user has a resonably large presence on reddit making 178 comments. Despite using neutural language it seems this user mostly post helpful comments, which results in a very high average upvote count bring his total upvotes to 3560. His high average upvotes indicates that this user only post comments which are help or constructive to the topic.

  This user is Likely atheist as that is the subreddit that the user visits the most. Anouther alternative is that he is a religous person who like to go on atheist subreddits and pick fights with other users, but this would reflect on his average upvotes and is likely not the case. This user is most likely in college either doing a master or is graduating soon based on the readablity of his comments. However this may be an issue with the Flesch-Kincaid algorithm as historical words and pronouns are possibly weighted quite low skewing the score. While this user does "like history" it seems he actually goes on history subreddits less than expected, having only 14% of his comment activity on history subreddits. But this also may be misleading as we do not have data on how often one visits a subreddit. It is possible history subreddits are in fact his most VISITED subreddits but based on his comment trends it is likely he will not comment unless the user has something contructive. This means that he may very much just enjoy reading through the subreddit and not post useless/meaningless comments like "haha" or "lol".

##Psychocoolguy
 
|Trait| Value|
|-------|-----------------------|
|User|Psychocoolguy|
|Toxic Score| -0.0024|
|Education | 33.8688|
|Comment Count | 129|
|Upvotes | 1.124|
|Location | null|
|Liked Subreddits | Fireteams:92, pokemongo:20, Smite:11, DestinyTheGame:4, Overwatch:2|

### What we know about Psychocoolguy
  From the table above se can see that he is mostly neutral in terms of toxicity, however unlike the previous users that we have identified it is likely because of polarizing comments the cancel each other out rather than the use of neutral langauge. This is becasue his average upvotes are relativly low telling us that he is likely getting some comments that are getting lots of upvotes and some getting lots of downvotes. His low readabilty score shows me that he is also makes is comments are not exactly short as it is much more difficult to get a low readabilty score when the user makes small comments. While overall he is mostly accepted it is likely that he does post upsetting comments from time to time keeping his overall average upvote count low dispite having a high comment count.
  
  It is clear by looking at the subreddits user comments on that he is a gamer. While estimating age with the previous users was relativly straight forword, estimating the age of psychocoolguy seems to be problematic as there are too many factors that can skew his readabity score. The best we can do to estimate this persons age is using the gamer demographic making this person likly a male between 15 and 30. Looking into the FireTeams subreddit it seems to be a community for gamers to get together. This implies that he may or may not have any friends in his friend group that is interested in the games he is interested in leading him to conduct his search on reddit.



#### [2 pt] A day in the life
You are a struggling scriptwriter trying to make it big in Hollywood. Find an interesting user with your backstory job, then trace their commenting activities across the site over time. Use this combination of data to build a story about the user’s life: what they do on a regular basis, who their friends are, their hopes/dreams, etc. You have some creative license here.

#### [2 pt] Matchmaker
While you work on your hit movie script, you need to pay the bills. Use your analysis skills to match up users with similar interests so that they can find love or friendship. If your algorithm is effective, you might just be able to pay rent this month!

  The matchmaker algorithm involved grouping user that have the similar readabilty scores and similar toxicity levels then sorting these user by thier average upvotes. While this was effective, the sample size that was obtained was too broad and had too many users with little or no information used make an effective match. First part of matching would require the removal of outliers in the data set. For this particular job the outliers would be users that do not comment enough, spam bots, and those with very large or very low readabilty scores. 
  
  We will use hadoop automatic sort to our advantage by automatically grouping users that are similar together. This allows for easier post processing after as to get users that have similiar reading and toxicity levels are a must before matching thier common interest. If a user is more educated and less toxic we can be a bit less strict with matching subreddits in contrast to the more toxic demographic which would require more similar interest. For simplicty our script will try to find the best match within 1000 users (500 upward and 500 downwards) and return the users with the most similiar interest (subreddits).
  

## trinkalkohol

|         |trinkalkohol   |vikkrell       |  
|---------|---------------|---------------|
|ReadScore|30.35          |31.62 |
|ToxicScore|.02099        |.00150|
|Upvotes | 1.989| 4.3333|
|Comment Count| 98 | 42|

|Subreddits in common|
|-------------------|
|IAmA|
|AskReddit|
|travel|

## HOWDYPARTNERBOT

|         |HOWDYPARTNERBOT   |InternetContrarian       |  
|---------|---------------|---------------|
|ReadScore|35.6055          |42.8 |
|ToxicScore|0        |.00335|
|Upvotes | -0.5| 11.3|
|Comment Count| 92 | 2081|

|Subreddits in common|
|-------------------|
|AskReddit|
|WTF|
|videos|
|AdviceAnimals|
|4chan|
|funny|

 
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
