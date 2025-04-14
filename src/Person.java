import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    public static Person fromCsvLine(String line) throws NegativeLifespanException{
        String[] columns = line.split(",");
        String[] flname = columns[0].split(" ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate bDay = null;
        LocalDate dDay = null;
        if (isNotEmpty(columns[1]))
            bDay = LocalDate.parse(columns[1], formatter);
        if (isNotEmpty(columns[2])){
            dDay = LocalDate.parse(columns[2], formatter);
        if (dDay.isBefore(bDay) && isNotEmpty(columns[1])) {
            throw new NegativeLifespanException(flname[0], flname[1]);
        }
    }
        Person person = new Person(flname[0], flname[1], bDay, dDay);
        return person;
    }

    public static boolean isNotEmpty(String s){
        return s != null && s != "" && s != " " && s != "   ";
    }

    public static List<Person> fromCsv(String path) {
        BufferedReader br = null;
        List<Person> people = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(path));
            String line;
            br.readLine();
            while((line = br.readLine())!=null){
                Person readPerson = fromCsvLine(line);
                for(Person existingPerson : people) {
                  if(!existingPerson.fname.equals(readPerson.fname) ||
                          !existingPerson.lname.equals(readPerson.lname)){

                  }
                }
                people.add(readPerson);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File doesn't exist");
        } catch (IOException e) {
            System.err.println("Error during reading file");
        } catch (NegativeLifespanException e) {
            System.err.println(e.getMessage());
        }
        return people;
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