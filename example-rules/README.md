#Example Rules Library
This example shows how to create a new rule (using Drools), and how to create new Drools' condition & consequence.

For more information please refer to documentation: https://www.jahia.com/community/extend/developers-techwiki/events-rules-jobs/rules

##Functional
The new rule automatically create Vanity URL (SEO) for pages having a depth equal or superior to 3, by shortening it
to "/news/NameOfTheNode"

To be more complete we should also create another rule for handling the modification of page's title.