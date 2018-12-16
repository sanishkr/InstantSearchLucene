package com.sns.lucene.Tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class.getName());

    public static class Person{
        public String getfName() {
            return fName;
        }

        public String getmName() {
            return mName;
        }

        public String getlName() {
            return lName;
        }

        String fName;
        String mName;

        Person(String fName, String mName, String lName) {
            this.fName = fName;
            this.mName = mName;
            this.lName = lName;
        }

        String lName;
    }

    public static List<Person> getAllPersons(){
        List<Person> personList;
        String csvpath = Utils.fetchProperties().getProperty("filepath");
        personList = csvReader(csvpath);
        return personList;
    }

    public static void main(String[] args) {
//        String csvpath = Utils.fetchProperties().getProperty("filepath");
//        csvReader(csvpath);
    }

    public static Properties fetchProperties(){
        Properties properties = new Properties();
        try {
            File file = ResourceUtils.getFile("classpath:application.properties");
            InputStream in = new FileInputStream(file);
            properties.load(in);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return properties;
    }

    private static List<Person> csvReader(String csvFile){
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ",";
        List<Person> personList = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] personName = line.split(cvsSplitBy);
                Person p = new Person(personName[0],personName[1],personName[2]);
                personList.add(p);
                //System.out.println("Name [first= " + personName[0] + ", middle=" + personName[1]+ ", last=" + personName[2] + "]");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return personList;
    }
}