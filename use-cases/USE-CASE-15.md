# USE CASE: 15 Produce a Report of the Most Populated Cities in a Country

## CHARACTERISTIC INFORMATION

### Goal in Context
As a Policy Maker I want the system to produce a report of the most populated cities in a country, based on a number I specify, so that I can support national urban development.

### Scope
Organisation.

### Level
Primary task.

### Preconditions
The database contains population data for all cities and their associated countries.

### Success End Condition
A report is available listing the specified number of the most populated cities within the selected country, ordered from largest to smallest population.

### Failed End Condition
No report is produced.

### Primary Actor
Policy Maker.

### Trigger
A request for a report of the most populated cities in a country is sent to the organisation.

## MAIN SUCCESS SCENARIO
1. Policy Maker selects a country and specifies the number of top populated cities to include in the report.
2. System retrieves and sorts city population data for that country from largest to smallest.
3. System generates the report and provides it to the Policy Maker for review.

## EXTENSIONS
**Invalid country or number specified:**
1. System prompts the Policy Maker to enter a valid country and/or number.

**Population data unavailable:**
1. System informs the Policy Maker that population data is unavailable and no report can be generated.

## SUB-VARIATIONS
None.

## SCHEDULE
**DUE DATE:** Release 1.0
