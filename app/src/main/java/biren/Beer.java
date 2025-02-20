package biren;

public record Beer (String name, Double rating, String type, double ABV){

    @Override
    public String toString(){
        return "{\n" + 
            "  \"name\": \"" + name + "\",\n" +
            "  \"rating\": " + rating + ",\n" +
            "  \"type\": \"" + type + "\",\n" +
            "  \"ABV\": " + ABV + "\n" +
            "},";
    }

    public String cmdToString(){
        return "name: " + name + " | rating: " + rating + " | type: " + type + " | ABV: " + ABV;
    }
}
