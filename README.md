# Final Health Project

## Description
FinalHealthProject is a web service designed to manage patients,
and their encounters within a healthcare setting.
The system offers CRUD functionalities for patient records.
This makes patient management and data retrieval seamless for healthcare professionals.

## Pre-Requisites
- JDK: Ensure you have a JDK installed on your machine.
- Postgres: ensure Postgres is installed and running on the default port (5432)

#### Postgres

This server requires that you have Postgres installed and running on the default Postgres port of 5432. 
It requires that you have a database created on the server with the name of `postgres`
- Your username should be `postgres`
- Your password should be `root`

## Getting Started

### Start the Server

Navigate to the AppRunner class within the project.
Right-click `AppRunner` and select `Run AppRunner.main()` to start the server.

### Connections

By default, this service starts up on port 8085 and accepts cross-origin requests from `*`.

### Usage (Endpoints)

### Patients

- GET /patients
Retrieves a list of all patients.

- GET /patients/{id}
Retrieves a patient by its unique ID.

- POST /patients
Creates a new patient. Requires patient details in the request body.

- PUT /patients/{id}
Updates a patient by its unique ID. Requires updated movie details in the request body.

- DELETE /patients/{id}
Deletes a patient by its unique ID.
- (Patients with existing encounters cannot be deleted)

### Encounters

- GET /patients/{id}/encounters
Retrieves encounters by a patient's unique ID

- POST /patients/{patient_id}/encounters
Creates a new encounter. Requires encounter details in the request body, and patient_id in the url.

- PUT /patients/{patient_id}/encounters/{encounter_id}
Updates a encounter by its unique ID, as well as the patient_id. Requires updated encounter details in the request body.

### Linting
- IntelliJ has built in linting. Light bulb icons will appear over or near code that requires attention.
Click these icons to see linting recommendations.

- You can also right-click anywhere in the code and analyze/code cleanup to lint the project or the file.

### Testing

- Navigate to the `test` package.
- Right-click the test class you wish to execute and select `Run with Coverage`
- Example test classes are `EncounterServiceImplTest` and `PatientServiceImplTest`

#### [Postman Collection Link](https://elements.getpostman.com/redirect?entityId=28096794-cfd84e39-ed9f-40dd-befb-28ba470ec244&entityType=collection)