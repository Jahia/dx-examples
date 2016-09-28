#Example Tag Library
This example provides how to create an Jahia Taglib.

##Tech Stack
- Jahia DX

##Usage
Install module and enable for site.  Drag `Display Tag` component onto a page.  Enter a name in the edit dialog for the `Display Tag`.

##Component

###Display Tag
This component executes the tag lib and tag function.
####Files: resources > exnt_actionSubmitter > html
- displayTag.jsp

##Java

###ExampleTag
This tag library outputs `Hello {{name}}`.

###Functions
This function outputs `Welcome {{name}}`.