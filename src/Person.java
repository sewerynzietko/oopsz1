import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Person implements Comparable<Person>{
    private String fname, lname;
    private LocalDate birthDate;
    private LocalDate deathDate;
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

    public Person(String fname, String lname, LocalDate birthDate, LocalDate deathDate) {
        this.fname = fname;
        this.lname = lname;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
        this.children = new TreeSet<>();
    }

    public static Person fromCsvLine(String line){
        String[] columns = line.split(",");
        String[] flname = columns[0].split(" ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.mm.yyyy");
        LocalDate bDay = null;
        LocalDate dDay = null;
        if(isNotEmpty(columns[1]))
            bDay = LocalDate.parse(columns[1],formatter);
        if(isNotEmpty(columns[2]))
            dDay = LocalDate.parse(columns[2],formatter);
        Person person = new Person(flname[0], flname[1], bDay, dDay);
        return person;
    }

    public static boolean isNotEmpty(String s){
        return s != null && s != "" && s != " " && s != "   ";
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