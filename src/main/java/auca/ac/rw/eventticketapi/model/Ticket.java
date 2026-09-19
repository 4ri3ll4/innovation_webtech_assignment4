package auca.ac.rw.eventticketapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ticket type is required")
    @Column(name = "ticket_type", nullable = false)
    private String ticketType;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    @Column(nullable = false)
    private Double price;

    @NotNull(message = "Quantity available is required")
    @PositiveOrZero(message = "Quantity available cannot be negative")
    @Column(name = "quantity_available", nullable = false)
    private Integer quantityAvailable;

    @NotNull(message = "Event reference is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", nullable = false)
    @JsonIgnoreProperties({"tickets"})
    private Event event;

    public Ticket() {}

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTicketType() { return ticketType; }
    public void setTicketType(String ticketType) { this.ticketType = ticketType; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getQuantityAvailable() { return quantityAvailable; }
    public void setQuantityAvailable(Integer quantityAvailable) { this.quantityAvailable = quantityAvailable; }
    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }
}
