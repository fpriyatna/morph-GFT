# Morph-GFT

Morph-GFT is an extension of Morph that works with Google Fusion Table (GFT) tables mapped with R2RML Mappings and enables users to query those tables using SPARQL queries. Underhood Morph-GFT, the SPARQL queries posed by the users are translated into SQL-like queries that are supported by GFT APIs. Unlike standard relational database implementation normally used with R2RML, GFT APIs do not support join operations. SPARQL-DQP is used to join the intermediate results and then the intermediate results are translated using the R2RML mappings specified by the users.
