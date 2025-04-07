import java.util.HashMap;
import java.util.Map;

public class Family {
    private Map<String, Person> people = new HashMap<>();

    public void add(Person personToAdd){
        String newKey = personToAdd.getFname() + " " + personToAdd.getLname();
        if(people.containsKey(newKey))
            return;
        people.put(newKey, personToAdd);
    }

    public void add(Person... peopleToAdd){
        for(Person p : peopleToAdd){
            add(p);
        }
    }

    public Person get(String key){
        return people.get(key);
    }
}
