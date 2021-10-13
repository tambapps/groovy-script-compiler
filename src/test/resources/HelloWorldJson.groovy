
def slurper = Class.forName('groovy.json.JsonSlurper').getConstructors()[0].newInstance()
def result = slurper.parseText('{"person":{"name":"Pierre","age":33,"pets":["dog","cat"]}}')
print result.person.name