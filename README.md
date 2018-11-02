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

#### [1 pt] Choose a day of significance to you (e.g., your birthday), and retrieve a 5% sample of the comments posted on this particular day across all 5 years of the dataset.
#### [1 pt] The number of comments posted per year will likely trend upward over time as more users join Reddit. Use feature scaling to normalize the number of comments per month from 0.0 to 1.0 and plot the values for each year. This way, we can isolate the proportion of comments across months. Do you notice any patterns?


The project specification defines several questions that you will answer with MapReduce jobs. You should edit this document (README.md) with your answers as you find them, including figures, references, etc.