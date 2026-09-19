package auca.ac.rw.eventticketapi.service;

import auca.ac.rw.eventticketapi.exception.ResourceNotFoundException;
import auca.ac.rw.eventticketapi.model.Event;
import auca.ac.rw.eventticketapi.model.Ticket;
import auca.ac.rw.eventticketapi.repository.EventRepository;
import auca.ac.rw.eventticketapi.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;

    public TicketService(TicketRepository ticketRepository, EventRepository eventRepository) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public List<Ticket> getTicketsByEvent(Long eventId) {
        // Confirm the event actually exists before listing its tickets
        if (!eventRepository.existsById(eventId)) {
            throw new ResourceNotFoundException("Event not found with id: " + eventId);
        }
        return ticketRepository.findByEventId(eventId);
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
    }

    public Ticket createTicket(Ticket ticket) {
        Event event = resolveEvent(ticket);
        ticket.setEvent(event);
        return ticketRepository.save(ticket);
    }

    public Ticket updateTicket(Long id, Ticket updatedTicket) {
        Ticket existing = getTicketById(id);
        Event event = resolveEvent(updatedTicket);
        existing.setTicketType(updatedTicket.getTicketType());
        existing.setPrice(updatedTicket.getPrice());
        existing.setQuantityAvailable(updatedTicket.getQuantityAvailable());
        existing.setEvent(event);
        return ticketRepository.save(existing);
    }

    public void deleteTicket(Long id) {
        Ticket existing = getTicketById(id);
        ticketRepository.delete(existing);
    }

    private Event resolveEvent(Ticket ticket) {
        if (ticket.getEvent() == null || ticket.getEvent().getId() == null) {
            throw new IllegalArgumentException("A valid event id must be provided for the ticket.");
        }
        return eventRepository.findById(ticket.getEvent().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Event not found with id: " + ticket.getEvent().getId()));
    }
}
