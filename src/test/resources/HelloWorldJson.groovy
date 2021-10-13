
def slurper = new groovy.json.JsonSlurper()
def result = slurper.parseText('{"person":{"name":"Pierre","age":33,"pets":["dog","cat"]}}')
print result.person.name