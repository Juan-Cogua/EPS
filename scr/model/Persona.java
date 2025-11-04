package model;

import excepciones.InvalidDataException;

/**
 * Clase base para personas en el sistema EPS.
 *
 * @author Juan Cogua
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
     * Constructor de Persona.
     *
     * @param name Nombre.
     * @param age Edad.
     * @param id Identificación.
     * @param bloodType Tipo de sangre.
     * @param address Dirección.
     * @param phone Teléfono.
     */
    public Persona(String name, byte age, String id, String bloodType, String address, String phone) {
        // Invariantes básicas
        if (name == null || name.trim().isEmpty()) throw new InvalidDataException("El nombre no puede estar vacío.");
        if (age < 0) throw new InvalidDataException("La edad no puede ser negativa.");
        if (id == null || id.trim().isEmpty()) throw new InvalidDataException("El ID no puede estar vacío.");
        if (bloodType == null || bloodType.trim().isEmpty()) throw new InvalidDataException("El tipo de sangre no puede estar vacío.");

        this.name = name;
        this.age = age;
        this.id = id;
        this.bloodType = bloodType;
        this.address = address;
        this.phone = phone;
    }

    /**
     * Obtiene el nombre de la persona.
     * @return El nombre.
     */
    public String getName() { return name; }
    
    /**
     * Establece el nombre de la persona.
     * @param name El nuevo nombre.
     */
    public void setName(String name) { this.name = name; }

    /**
     * Obtiene la edad de la persona.
     * @return La edad.
     */
    public byte getAge() { return age; }
    
    /**
     * Establece la edad de la persona.
     * @param age La nueva edad.
     */
    public void setAge(byte age) { this.age = age; }

    /**
     * Obtiene la identificación de la persona.
     * @return La identificación (ID).
     */
    public String getId() { return id; }
    
    /**
     * Establece la identificación de la persona.
     * @param id La nueva identificación.
     */
    public void setId(String id) { this.id = id; }

    /**
     * Obtiene el tipo de sangre de la persona.
     * @return El tipo de sangre.
     */
    public String getBloodType() { return bloodType; }
    
    /**
     * Establece el tipo de sangre de la persona.
     * @param bloodType El nuevo tipo de sangre.
     */
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }

    /**
     * Obtiene la dirección de la persona.
     * @return La dirección.
     */
    public String getAddress() { return address; }
    
    /**
     * Establece la dirección de la persona.
     * @param address La nueva dirección.
     */
    public void setAddress(String address) { this.address = address; }

    /**
     * Obtiene el teléfono de la persona.
     * @return El teléfono.
     */
    public String getPhone() { return phone; }
    
    /**
     * Establece el teléfono de la persona.
     * @param phone El nuevo teléfono.
     */
    public void setPhone(String phone) { this.phone = phone; }

    /**
     * Devuelve un resumen de la persona.
     * @return Resumen como String.
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
     * Valida si la persona es mayor de edad (18 años o más).
     * Autor: Andrés Rojas.
     * @return {@code true} si la edad es 18 o mayor, {@code false} en caso contrario.
     */
    public boolean esMayorDeEdad() {
        return age >= 18;
    }
    /**
     * Actualiza la información de contacto (dirección y teléfono) de la persona.
     * Autor: Andrés Rojas.
     * @param nuevaDireccion La nueva dirección de residencia.
     * @param nuevoTelefono El nuevo número de teléfono de contacto.
     */
    public void actualizarContacto(String nuevaDireccion, String nuevoTelefono) {
        this.address = nuevaDireccion;
        this.phone = nuevoTelefono;
    }

     /**
     * Redefine el método equals para comparar objetos Persona basándose en su ID.
     * Dos objetos Persona son considerados iguales si tienen el mismo ID.
     * Autor: Andrés Rojas.
     * @param obj El objeto a comparar.
     * @return {@code true} si el objeto es una Persona con el mismo ID, {@code false} en caso contrario.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; 
        if (obj == null || getClass() != obj.getClass()) return false;
        Persona persona = (Persona) obj;
        return id.equals(persona.id); 
    }

     /**
     * Redefine el método hashCode para mantener la consistencia con equals.
     * El hash code se calcula basándose en el ID.
     * Autor: Andrés Rojas.
     * @return El valor del hash code.
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
