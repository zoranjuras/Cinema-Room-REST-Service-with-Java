package cinema.rest;

import cinema.dto.*;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class CinemaController {

    private ProjectionRoom projectionRoom = new ProjectionRoom(9, 9);
    private Map<UUID, Seat> purchasedTickets = new HashMap<>();

    @GetMapping("/seats")
    public ProjectionRoom seats() {
        return projectionRoom;
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseSeat(@RequestBody Map<String, Integer> request) {
        int row = request.get("row");
        int column = request.get("column");

        if (row <= 0 || column <= 0 || row > projectionRoom.getRows() || column > projectionRoom.getColumns()) {
            return ResponseEntity.badRequest().body(Map.of("error", "The number of a row or a column is out of bounds!"));
        }

        Seat seat = projectionRoom.getSeats().stream()
                .filter(s -> s.getRow() == row && s.getColumn() == column)
                .findFirst()
                .orElse(null);

        if (seat == null || (seat.isPurchased() && seat.getUuid() != null)) {
            return ResponseEntity.badRequest().body(Map.of("error", "The ticket has been already purchased!"));
        }

        UUID transactionId = UUID.randomUUID();
        seat.setPurchased(true);
        seat.setUuid(transactionId); // Set UUID for the seat

        purchasedTickets.put(transactionId, seat); // Update purchasedTickets map

        Map<String, Object> response = new HashMap<>();
        response.put("token", transactionId.toString());
        response.put("ticket", Map.of(
                "row", seat.getRow(),
                "column", seat.getColumn(),
                "price", seat.getPrice()
        ));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/return")
    public ResponseEntity<?> refundTicket(@RequestBody Map<String, String> request) {
        UUID token = UUID.fromString(request.get("token"));
        Seat seat = purchasedTickets.get(token);

        if (seat != null && seat.isPurchased()) {
            seat.setPurchased(false);
            seat.setUuid(null);
            purchasedTickets.remove(token);

            return ResponseEntity.ok(Map.of(
                    "ticket", Map.of(
                            "row", seat.getRow(),
                            "column", seat.getColumn(),
                            "price", seat.getPrice()
                    )
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Wrong token!"));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(@RequestParam(required = false) String password) {
        if (password == null || !password.equals("super_secret")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "The password is wrong!"));
        }

        int totalIncome = purchasedTickets.values().stream()
                .mapToInt(Seat::getPrice)
                .sum();
        int totalPurchased = purchasedTickets.size();
        int totalAvailable = projectionRoom.getSeats().size() - totalPurchased;

        return ResponseEntity.ok(Map.of(
                "income", totalIncome,
                "available", totalAvailable,
                "purchased", totalPurchased
        ));
    }

}
