Diseases lexicon

Resources:
•	Unified Medical Language System (UMLS) Metathesaurus
•	Systematized Nomenclature of Medicine -- Clinical Terms (SNOMED - CT)

UMLS Metathesaurus:
Compiling UMLS Metathesaurus is a separate project. Please see our project at https://github.com/CutaneousBioinf/LiteratureMiningTool/tree/master/ConceptMap/UMLSMetathesaurusCompiler

Diseases and synonyms can be retrived based on the UMLS semantic types related to DISORDER. We developed a Java program to achieve the same.  

$ javac DiseaseDictionaryGenerator.java
$ java DiseaseDictionaryGenerator ALL_CONCEPTS_FILE UMLS_DISEASE_FILE 
 
SNOMED - CT:
We need UMLS Metathesaurus license for downloading and using SNOMED CT. The resource can be downloaded from UMLS Terminology Services (https://uts.nlm.nih.gov//home.html). Download UMLS Metathesaurus and install on server by selecting the option SNOMED CT.

Processing MRCONSO.RRF a file:
$ javac MRCONSOPreferredVocabularyGenerator.java
$ java MRCONSOPreferredVocabularyGenerator ~/MRCONSO.RRF OUTPUT_FILE1

Processing MRSTY.RRF a file:
A. Extraction of unique concept identifier (CUI) and unique semantic type identifier (TUI)
$ javac MRSTYCuiTuiGenerator.java
$ java MRSTYCuiTuiGenerator ~/MRSTY.RRF OUTPUT_FILE2

B. Grouping of concepts with same CUI, but different TUI
$ javac MRSTYCuiTuiGrouper.java
$ java MRSTYCuiTuiGrouper OUTPUT_FILE2 OUTPUT_FILE3

The processed files from MRCONSO.RRF and MRSTY.RRF are used for generating diseases lexicon. We need semantic group file (i.e. SemGroups_2018.txt) which can be downloaded from NCBI MetaMap (https://metamap.nlm.nih.gov/Docs/SemGroups_2018.txt). The execution may takes several hours depending on the system or server used to run the program. 

$ javac MRCONSODictionaryPreparer.java
$ java OUTPUT_FILE3 SemGroups_2018.txt OUTPUT_FILE1 OUTPUT_FILE4

We developed a Java program to extract disease concepts from SNOMED CT. 
$ javac DiseaseDictionaryGenerator.java
$ java DiseaseDictionaryGenerator OUTPUT_FILE4 SNOEMED_DISEASES_FILE

UMLS Metathesaurus + SNOMED CT:
SNOMED CT is from UMLS Metathesaurus only. We assume that the disease concepts from both resources may overlap. We also noticed that the concepts have same CUI_TUI. For example, ‘acute abdominal pain syndrome’ has CUI_TUI: C0000727_T184 in both resources.

We combined the resources using Linux command.
$ cat UMLS_DISEASE_FILE SNOEMED_DISEASES_FILE | sort | uniq -i > DISEASES_LEXICON_v1

Post processing of diseases lexicon:
We noticed stop words as disease synonyms (e.g. ‘In’ for ‘Present’). Such synonyms will give false frequency count in the literature. Therefore, it is good to remove the disease synonyms that are actually stop words.

$ javac StopwordsAsDiseaseSynonyms.java
$ java StopwordsAsDiseaseSynonyms DISEASES_LEXICON_v1 STOPWORDS_AS_DISEASES_AND_SYNONYMS

$ javac StopwordsAsDiseaseSynonymsRemover.java
$ java StopwordsAsDiseaseSynonymsRemover STOP_WORDS DISEASES_LEXICON_v1 DISEASES_LEXICON_v1_NO_STOPWORDS

We collected stopwords from our previous work and various resources. File: stopwords.txt

We noticed that certain disease concepts map to multiple CUIs. This will lead to independent counts for each CUI. Therefore, we combined the multiple CUIs together and assigned a customized CUI.

$ javac DiseasesWithMultipleIDRetriever.java
$ javac DiseasesWithMultipleIDRetriever DISEASES_LEXICON_v1_NO_STOPWORDS DISEASES_WITH_MULTIPLE_IDs  

$ javac DiseasesWithMultipleCUICompiler.java
$ java DiseasesWithMultipleCUICompiler DISEASES_LEXICON_v1_NO_STOPWORDS DISEASES_WITH_MULTIPLE_IDs OUTPUT_FILE5

$ javac DiseasesWithModifiedCUIUpdater.java
$ java DiseasesWithModifiedCUIUpdater OUTPUT_FILE5 DISEASES_LEXICON_v1_NO_STOPWORDS DISEASES_LEXICON_v2
