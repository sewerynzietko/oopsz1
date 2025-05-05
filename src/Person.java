import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Person implements Comparable<Person>, Serializable {
    private String fname, lname;
    private LocalDate birthDate;

    private LocalDate deathDate;
    private Set<Person> children;
    private Person mother;
    private Person father;
    public boolean adopt(Person child){
        return children.add(child);
    }

    public Person(String fname, String lname, LocalDate birthDate, LocalDate deathDate) {
        this.fname = fname;
        this.lname = lname;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
        this.children = new TreeSet<>();
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
    public List<Person> getChildren(){
        return List.copyOf(children);
    }

    @Override
    public int compareTo(Person o) {
        return this.birthDate.compareTo(o.birthDate);
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(LocalDate deathDate) {
        this.deathDate = deathDate;
    }

    public static Person fromCsvLine(String line) throws NegativeLifespanException {
        String[] colums = line.split(",");
        String[] flname = colums[0].split(" ");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate birthDate = null;
        LocalDate deathDate = null;
        if (isNotEmpty(colums[1])){
            birthDate = LocalDate.parse(colums[1], formatter);
        }
        if (isNotEmpty(colums[2])){
            deathDate = LocalDate.parse(colums[2], formatter);
            if (isNotEmpty(colums[1]) && deathDate.isBefore(birthDate)){
                throw new NegativeLifespanException(flname[0], flname[1]);
            }
        }
        return new Person(flname[0], flname[1], birthDate, deathDate);
    }

    public static List<Person> fromCsv(String path){
        List<Person> people = new ArrayList<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            br.readLine();
            while((line = br.readLine()) != null){
                Person readPerson = fromCsvLine(line);
                if (people.size() == 0){
                    people.add(readPerson);
                }
                for (int i = 0; i < people.size(); i++){
                    Person existingPerson = people.get(i);
                    if(!existingPerson.fname.equals(readPerson.fname) ||
                            !existingPerson.lname.equals(readPerson.lname)){
                        people.add(readPerson);
                    }
                    if(!existingPerson.fname.equals(readPerson.fname) ||
                            !existingPerson.lname.equals(readPerson.lname)){
                        people.add(readPerson);
                    }
                    if(!existingPerson.fname.equals(readPerson.fname) ||
                            !existingPerson.lname.equals(readPerson.lname)){
                        people.add(readPerson);
                    }
                }

            }
        } catch (FileNotFoundException e) {
            System.err.println("File doesn't exist");
        } catch (IOException e) {
            System.err.println("Error during reading file");
        } catch (NegativeLifespanException e){
            System.err.println(e.getMessage());
        }
        return people;
    }

    public String toUMLObject(){
        Function<String, String> addQuotes = text -> "\"" + text + "\"";
        Function<Person, String> getFullnameWithSpace = pipla -> pipla.fname + " " + pipla.lname;
        Function<Person, String> getFullName = pipla -> pipla.fname + pipla.lname;
        Function<Person, String> toUMLline = pipla ->
                String.format("object " + addQuotes.apply(getFullnameWithSpace.apply(pipla))
                        + " as " + getFullName.apply(pipla));
        return toUMLline.apply(this);
    }

    public String toUMLRelation(){
        Function<Person, String> getFullName = pipla -> pipla.fname + pipla.lname;
        Function<Person, String> getFatherRelation = pipla -> {
            if(pipla.father != null)
                return getFullName.apply(pipla) + " <-- " + getFullName.apply(pipla.father);
            return "";
        };
        Function<Person, String> getMotherRelation = pipla -> {
                if(pipla.mother != null)
                    return getFullName.apply(pipla) + " <-- " + getFullName.apply(pipla.mother);
                return "";
        };
        Function<Person, String> getAllRelations = pipla -> {
            String motherString = getMotherRelation.apply(this);
            String fatherString = getFatherRelation.apply(this);
            if(motherString != "" && fatherString != "")
                return motherString + "\n" + fatherString;
            return motherString + fatherString;
        };
        return getAllRelations.apply(this);
    }

    public static boolean isNotEmpty(String s){
        return s != null && s != "" && s != " " && s != "\t";
    }

    public void setFather(Person parent) {
        this.father = parent;
    }

    public static String toUmlList(List<Person> people){
        Function<List<Person>, String> convertToUML = list -> {
            String openingTag = "@startuml";
            String endingTag = "@enduml";
            List<String> objectLines = list.stream().map(pipla -> pipla.toUMLObject()).collect(Collectors.toList());
            List<String> relationLines = list.stream().map(pipla -> pipla.toUMLRelation()).collect(Collectors.toList());
            return openingTag + "\n" + String.join("\n",objectLines) + String.join("\n",relationLines) + endingTag;
        };
        return toUmlList(people);
    }

    public static List<Person> filterList(List<Person> people, String key){
        Function<Person, String> getFullName = pipla -> pipla.fname + pipla.lname;
        return people.stream().filter(pipla -> getFullName.apply(pipla).contains(key)).collect(Collectors.toList());
    }

    public static List<Person> sortPeopleByBirthyear(List<Person> people) {
        return people.stream().sorted().collect(Collectors.toList());
    }

    public static void saveToBinaryFile(List<Person> people, String path){
        try{
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(people);
            oos.close();
        }
        catch(FileNotFoundException e){
            throw new RuntimeException(e);
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Person> readFromBinaryFile(String path){
        List<Person> people = new ArrayList<>();
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
            Object obj = ois.readObject();
            if(obj instanceof List){
                List<?> list = (List<?>) obj;
                for(Object o : list){
                    if(o instanceof Person){
                        people.add((Person) o);
                    }
                }
            }
            ois.close();
        }
        catch(FileNotFoundException e){
            throw new RuntimeException(e);
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
        catch(ClassNotFoundException e){
            throw new RuntimeException(e);
        }
        return people;
    }
}
