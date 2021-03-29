package org.apache.ibatis.javabase.statictype;

import java.util.HashMap;
import java.util.Map;

/**
 * @author heqin
 */
public class MappedStatement {

    private String resource;

    private String id;

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class Builder {

        private MappedStatement mappedStatement = new MappedStatement();

        private static Map<String,Object> options = new HashMap<String,Object>();

        static {
            options.putIfAbsent("name","JoJo");
        }

        public Builder(String id){
            mappedStatement.id = id;
        }

        public Builder resource(String resource) {
            mappedStatement.resource = resource;
            return this;
        }

        public void changeOption(){
            options.putIfAbsent("age",2);
        }
        public void logOption(){
            System.out.println("options: " + options);
        }
    }
}
