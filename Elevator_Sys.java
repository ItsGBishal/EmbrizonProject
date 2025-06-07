public package Project_3;

// Simplified, corrected, and fully working version of single-threaded Elevator system in Java

import java.util.*;

enum Direction {
    UP, DOWN, IDLE
}

enum ElevatorStatus {
    MOVING, STOPPED, MAINTENANCE
}

class Request {
    private final int floor;
    private final Direction direction;

    public Request(int floor, Direction direction) {
        this.floor = floor;
        this.direction = direction;
    }

    public int getFloor() {
        return floor;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Request request = (Request) obj;
        return floor == request.floor && direction == request.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(floor, direction);
    }
}

class Elevator {
    private final int minFloor = 0;
    private final int maxFloor = 10;
    private int currentFloor = 0;
    private Direction direction = Direction.IDLE;
    private ElevatorStatus status = ElevatorStatus.STOPPED;

    private final TreeSet<Integer> upRequests = new TreeSet<>();
    private final TreeSet<Integer> downRequests = new TreeSet<>(Collections.reverseOrder());

    public void requestFloor(int floor) {
        if (floor == currentFloor) {
            openAndCloseDoors();
            return;
        }

        if (floor > currentFloor) {
            upRequests.add(floor);
        } else {
            downRequests.add(floor);
        }

        processRequests();
    }

    public void callElevator(int floor, Direction dir) {
        requestFloor(floor); // same logic
    }

    private void processRequests() {
        while (hasRequests() && status != ElevatorStatus.MAINTENANCE) {
            moveToNextFloor();
        }
    }

    private void moveToNextFloor() {
        int nextFloor = getNextFloor();
        if (nextFloor == currentFloor) {
            direction = Direction.IDLE;
            status = ElevatorStatus.STOPPED;
            return;
        }

        direction = (nextFloor > currentFloor) ? Direction.UP : Direction.DOWN;
        status = ElevatorStatus.MOVING;

        System.out.println("Moving " + direction + " to floor " + nextFloor);
        simulateMovement();
        currentFloor = nextFloor;
        status = ElevatorStatus.STOPPED;

        if (shouldStopAtFloor(currentFloor)) {
            stopAtFloor(currentFloor);
        }
    }

    private int getNextFloor() {
        if (upRequests.contains(currentFloor) || downRequests.contains(currentFloor)) {
            return currentFloor;
        }

        if (direction == Direction.UP || direction == Direction.IDLE) {
            Optional<Integer> up = upRequests.stream().filter(f -> f > currentFloor).findFirst();
            if (up.isPresent()) return up.get();
            Optional<Integer> down = downRequests.stream().filter(f -> f < currentFloor).findFirst();
            return down.orElse(currentFloor);
        } else {
            Optional<Integer> down = downRequests.stream().filter(f -> f < currentFloor).findFirst();
            if (down.isPresent()) return down.get();
            Optional<Integer> up = upRequests.stream().filter(f -> f > currentFloor).findFirst();
            return up.orElse(currentFloor);
        }
    }

    private boolean shouldStopAtFloor(int floor) {
        return upRequests.contains(floor) || downRequests.contains(floor);
    }

    private void stopAtFloor(int floor) {
        System.out.println("Stopping at floor " + floor);
        upRequests.remove(floor);
        downRequests.remove(floor);
        openAndCloseDoors();
    }

    private void openAndCloseDoors() {
        System.out.println("Opening doors at floor " + currentFloor);
        simulateDoor();
        System.out.println("Closing doors.");
    }

    private void simulateMovement() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void simulateDoor() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private boolean hasRequests() {
        return !upRequests.isEmpty() || !downRequests.isEmpty();
    }

    public void displayStatus() {
        System.out.println("Floor: " + currentFloor + ", Direction: " + direction + ", Status: " + status);
    }

    public void setMaintenanceMode(boolean mode) {
        this.status = mode ? ElevatorStatus.MAINTENANCE : ElevatorStatus.STOPPED;
        System.out.println("Elevator is now " + (mode ? "under maintenance" : "active"));
    }
}

public class Elevator_Sys {
    public static void main(String[] args) {
        Elevator elevator = new Elevator();

        System.out.println("Initial status:");
        elevator.displayStatus();

        System.out.println("\nRequesting 5, 2, 8:");
        elevator.requestFloor(5);
        elevator.requestFloor(2);
        elevator.requestFloor(8);

        elevator.displayStatus();

        System.out.println("\nPutting into maintenance mode:");
        elevator.setMaintenanceMode(true);
        elevator.requestFloor(4); // should not process

        System.out.println("\nResuming service:");
        elevator.setMaintenanceMode(false);
        elevator.requestFloor(4);

        elevator.displayStatus();
    }
}

 Elevator1_Sys {
  
}
