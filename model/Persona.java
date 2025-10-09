package model;

/**
 * Clase que representa a una persona con información básica como nombre, edad, identificación, tipo de sangre, dirección y teléfono.
 * Sirve como clase base para otras entidades del sistema.
 * 
 * @author Juan Cogua
 * @author Andres Rojas
 * @version 1.0
 */
class Persona {
    private String name;
    private byte age;
    private String id;
    private String bloodType;
    private String address;
    private String phone;

    /**
     * Constructor de la clase Persona.
     * 
     * @param name Nombre de la persona
     * @param age Edad de la persona
     * @param id Identificación de la persona
     * @param bloodType Tipo de sangre
     * @param address Dirección
     * @param phone Teléfono
     */
    public Persona(String name, byte age, String id, String bloodType, String address, String phone) {
        this.name = name;
        this.age = age;
        this.id = id;
        this.bloodType = bloodType;
        this.address = address;
        this.phone = phone;
    }

    /**
     * Obtiene el nombre de la persona.
     * 
     * @return Nombre de la persona
     */
    public String getName() { 
        return name; 
    }

    /**
     * Establece el nombre de la persona.
     * 
     * @param name Nuevo nombre
     */
    public void setName(String name) { 
        this.name = name; 
    }

    /**
     * Obtiene la edad de la persona.
     * 
     * @return Edad de la persona
     */
    public byte getAge() { 
        return age; 
    }

    /**
     * Establece la edad de la persona.
     * 
     * @param age Nueva edad
     */
    public void setAge(byte age) { 
        this.age = age; 
    }

    /**
     * Obtiene la identificación de la persona.
     * 
     * @return ID de la persona
     */
    public String getId() { 
        return id; 
    }

    /**
     * Establece la identificación de la persona.
     * 
     * @param id Nueva identificación
     */
    public void setId(String id) { 
        this.id = id; 
    }

    /**
     * Obtiene el tipo de sangre de la persona.
     * 
     * @return Tipo de sangre
     */
    public String getBloodType() { 
        return bloodType; 
    }

    /**
     * Establece el tipo de sangre de la persona.
     * 
     * @param bloodType Nuevo tipo de sangre
     */
    public void setBloodType(String bloodType) { 
        this.bloodType = bloodType; 
    }

    /**
     * Obtiene la dirección de la persona.
     * 
     * @return Dirección
     */
    public String getAddress() { 
        return address; 
    }

    /**
     * Establece la dirección de la persona.
     * 
     * @param address Nueva dirección
     */
    public void setAddress(String address) { 
        this.address = address; 
    }

    /**
     * Obtiene el número de teléfono de la persona.
     * 
     * @return Teléfono
     */
    public String getPhone() { 
        return phone; 
    }

    /**
     * Establece el número de teléfono de la persona.
     * 
     * @param phone Nuevo teléfono
     */
    public void setPhone(String phone) { 
        this.phone = phone; 
    }

    /**
     * Muestra un resumen con la información básica de la persona.
     * 
     * @return Cadena con la información resumida
     */
    public String resumen() {
        return "Nombre: " + name +
               ", ID: " + id +
               ", Edad: " + age +
               ", Tipo de sangre: " + bloodType +
               ", Direccion: " + address +
               ", Telefono: " + phone;
    }

    /**
     * Verifica si la persona es mayor de edad.
     * 
     * @return true si es mayor de edad, false en caso contrario
     */
    public boolean esMayorDeEdad() {
        return age >= 18;
    }

    /**
     * Actualiza la información de contacto de la persona.
     * 
     * @param nuevaDireccion Nueva dirección
     * @param nuevoTelefono Nuevo número de teléfono
     */
    public void actualizarContacto(String nuevaDireccion, String nuevoTelefono) {
        this.address = nuevaDireccion;
        this.phone = nuevoTelefono;
    }

    /**
     * Compara si dos objetos Persona son iguales según su ID.
     * 
     * @param obj Objeto a comparar
     * @return true si tienen el mismo ID, false en caso contrario
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; 
        if (obj == null || getClass() != obj.getClass()) return false;
        Persona persona = (Persona) obj;
        return id.equals(persona.id); 
    }

    /**
     * Genera un código hash basado en el ID de la persona.
     * 
     * @return Código hash de la persona
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
