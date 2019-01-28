import requests


#The HTTP api limits the requests to 30 incorect words. As a result we may want to have this script divide the text up into sentences and spell check them each individually.
#Could Multithread this to decrease time it takes to search for larger documents


#These are the params that get passed into the api
#Languages and text are required but there are other options
post_params = {
	"text": "thiss is a test",
	"language": "en-US",
}

#This is the part that does the spell checking
r = requests.post('https://languagetool.org/api/v2/' + 'check', data=post_params)

#This outputs the results
print(r.json())

#Instead of printing the results we may want to instead parse them and output them in a cleaner way than the messy json that it is now

#Possible requirements
#Create gui that can eiter be typed in or be given a text document
#Multi-thread the searching of each sentence
#Visiual indication of which words are mispelled and recommended corrections
#Ability to correct the words and output the corrected text to an output file


