package auca.ac.rw.eventticketapi.service;

import auca.ac.rw.eventticketapi.exception.DuplicateResourceException;
import auca.ac.rw.eventticketapi.exception.ResourceNotFoundException;
import auca.ac.rw.eventticketapi.model.Event;
import auca.ac.rw.eventticketapi.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
    }

    public Event createEvent(Event event) {
        // Business rule: no duplicate event titles
        if (eventRepository.existsByTitleIgnoreCase(event.getTitle())) {
            throw new DuplicateResourceException("An event with this title already exists: " + event.getTitle());
        }
        // Business rule: sanity bound on how far in the future an event can be
        if (event.getEventDate().getYear() > LocalDate.now().getYear() + 10) {
            throw new IllegalArgumentException("Event date is too far in the future to be realistic.");
        }
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event updatedEvent) {
        Event existing = getEventById(id);
        if (eventRepository.existsByTitleIgnoreCaseAndIdNot(updatedEvent.getTitle(), id)) {
            throw new DuplicateResourceException("An event with this title already exists: " + updatedEvent.getTitle());
        }
        existing.setTitle(updatedEvent.getTitle());
        existing.setDescription(updatedEvent.getDescription());
        existing.setEventDate(updatedEvent.getEventDate());
        existing.setStartTime(updatedEvent.getStartTime());
        existing.setCategory(updatedEvent.getCategory());
        return eventRepository.save(existing);
    }

    public void deleteEvent(Long id) {
        Event existing = getEventById(id);
        eventRepository.delete(existing);
    }
}
