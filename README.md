# WildSim - Ecosystem Simulation Platform

![image](https://github.com/user-attachments/assets/86a63e31-3a8a-45ed-b546-646a8e1965f0)

## Overview

WildSim is a ecosystem simulation application built in Java that models biological interactions between different organisms in a virtual environment. The project demonstrates advanced object-oriented programming principles, real-time visualization, and database integration to create a dynamic ecosystem where plants, herbivores, and carnivores interact according to realistic biological behaviors.

## Key Features

### Interactive Ecosystem Simulation
- **Real-time visualization** using JavaFX with custom graphics rendering
- **Dynamic ecosystem evolution** with configurable simulation parameters
- **Multi-organism interactions** including predator-prey relationships

### Advanced Architecture
- **Object-oriented design** with inheritance hierarchies for organisms and environments
- **Singleton pattern** implementation for service management
- **Strategy pattern** for different organism behaviors
- **MVC architecture** separating business logic from presentation

### Data Persistence & Management
- **MongoDB integration** for persistent data storage
- **CRUD operations** with dedicated management interface
- **CSV logging system** for activity tracking and analysis
- **Real-time database synchronization** during simulation steps

### Organism Behavior System
- **Intelligent movement algorithms** with vision range and pathfinding
- **Energy-based life cycles** with realistic survival mechanics
- **Adaptive feeding behaviors** specific to organism types
- **Population dynamics** with birth, death, and evolution tracking

## System Architecture

### Core Components

```
WildSim/
├── src/main/java/com/wildsim/
│   ├── Main.java                    # Application entry point
│   ├── config/                      # Configuration classes
│   ├── environment/                 # Ecosystem and positioning
│   ├── model/
│   │   ├── organisms/              # Animal and plant hierarchies  
│   │   └── environment/            # Environmental elements
│   ├── service/                    # Business logic services
│   ├── ui/                         # JavaFX user interface
│   └── mongodb/                    # Database utilities
├── src/main/resources/
│   └── images/                     # Organism sprites and textures
├── docker-compose.yml              # MongoDB container configuration
├── build.gradle                    # Gradle build configuration
└── README.md
```

### Design Patterns Implemented

- **Singleton Pattern**: Database and service management
- **Factory Pattern**: Organism creation and initialization
- **Observer Pattern**: UI updates and event handling
- **Strategy Pattern**: Different behavioral algorithms for organisms

## Organism Types & Behaviors

### Plants (Trees)
- **Energy Generation**: Autonomous growth and energy production
- **Reproduction**: Threshold-based propagation
- **Ecosystem Role**: Primary producers and food source

### Herbivores
- **Foraging Behavior**: Intelligent plant-seeking with vision range
- **Movement**: Strategic pathfinding to food sources
- **Survival Mechanics**: Energy consumption and predator avoidance

### Carnivores
- **Hunting Behavior**: Advanced predator algorithms
- **Prey Selection**: Optimal target identification within vision range
- **Mercy Mechanics**: Probabilistic prey survival (20% chance)

## Technical Stack

### Core Technologies
- **Java 11+**: Primary development language
- **JavaFX**: Rich desktop application framework
- **MongoDB**: NoSQL database for data persistence
- **Gradle**: Build automation and dependency management
- **Docker**: Containerized database deployment

### Key Libraries & Frameworks
- **MongoDB Java Driver**: Database connectivity
- **JavaFX Canvas**: Custom graphics rendering
- **BSON**: Document serialization and deserialization

## Installation & Setup

### Prerequisites
- Java Development Kit (JDK) 11 or higher
- Docker and Docker Compose
- Gradle 7+

### Quick Start
```bash
# Clone the repository
git clone https://github.com/andreiOpran/WildSim.git
cd WildSim

# Start MongoDB container
docker-compose up -d

# Build and run the application
gradle clean build
gradle run
```

### Database Configuration
The application connects to MongoDB running in a Docker container with the following settings:
- **Host**: localhost:27017 (via Docker)
- **Database**: wildsim
- **Authentication**: root/root (admin database)

## Usage

### Simulation Controls
1. **Initialize Ecosystem**: Set organism populations and environment parameters
2. **Run Simulation**: Execute evolution steps with real-time visualization
3. **Monitor Progress**: Track population dynamics and ecosystem health
4. **Data Management**: Use CRUD interface for detailed organism management

### CRUD Operations
- **Create**: Add new organisms with custom parameters
- **Read**: View detailed organism statistics and positions
- **Update**: Modify organism properties during simulation
- **Delete**: Remove organisms from the ecosystem

## Learning Outcomes & Skills Demonstrated

### Programming Concepts
- **Advanced OOP**: Inheritance, polymorphism, encapsulation, abstraction
- **Design Patterns**: Singleton, Factory, Strategy, Observer
- **Data Structures**: Collections, matrices, spatial indexing
- **Algorithms**: Pathfinding, collision detection, optimization

### Software Engineering
- **Clean Architecture**: Separation of concerns, modularity
- **Database Design**: NoSQL schema design, CRUD operations
- **UI/UX Development**: Interactive desktop applications
- **DevOps Practices**: Containerization with Docker
- **Build Automation**: Gradle build system

### Domain Knowledge
- **Biological Modeling**: Ecosystem dynamics, population biology
- **Simulation Theory**: Agent-based modeling, emergent behavior
- **Mathematical Modeling**: Energy systems, probability distributions
