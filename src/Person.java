import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Person implements Comparable<Person>{
    private String fname, lname;
    private LocalDate birthDate;
    private Set<Person> children;
    public boolean adopt(Person child){
        return children.add(child);
    }

    public Person(String fname, String lname, LocalDate birthDate) {
        this.fname = fname;
        this.lname = lname;
        this.birthDate = birthDate;
        this.children = new TreeSet<>();
    }

    public Person getYoungestChild(){
//        if(children.isEmpty())
//            return null;
        LocalDate youngestChildAge = LocalDate.MIN;
        Person youngestChild = null;
        for (Person child : children){
            if(child.birthDate.isAfter(youngestChildAge)) {
                youngestChildAge = child.birthDate;
                youngestChild = child;
            }
        }
        return youngestChild;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    @Override
    public int compareTo(Person o) {
        return this.birthDate.compareTo(o.birthDate);
    }

    public List<Person> getChildren()
    {
        return List.copyOf(children);
    }
}