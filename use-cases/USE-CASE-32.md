# USE CASE: 32 Produce a Report Showing Global Language Distribution

## CHARACTERISTIC INFORMATION

### Goal in Context
As a Data Analyst I want the system to produce a report showing the number of people who speak Chinese, English, Hindi, Spanish, and Arabic, ordered from largest to smallest, including their percentage of the world population so that I can understand global language distribution.

### Scope
Organisation.

### Level
Primary task.

### Preconditions
The database contains global population data and language demographic data.

### Success End Condition
A report is available showing the number and percentage of people who speak Chinese, English, Hindi, Spanish, and Arabic, ordered from largest to smallest.

### Failed End Condition
No report is produced.

### Primary Actor
Data Analyst.

### Trigger
A request for a report showing the global distribution of major languages is sent to the organisation.

## MAIN SUCCESS SCENARIO
1. Data Analyst requests a report showing the number and percentage of people who speak Chinese, English, Hindi, Spanish, and Arabic.
2. System retrieves and calculates population figures and percentages for each language.
3. System generates the report, ordering the languages from largest to smallest, and provides it to the Data Analyst for review.

## EXTENSIONS
**Language data incomplete or unavailable:**
1. System informs the Data Analyst that language data is incomplete or unavailable and no report can be generated.

## SUB-VARIATIONS
None.

## SCHEDULE
**DUE DATE:** Release 1.0
