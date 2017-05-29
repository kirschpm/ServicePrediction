#!/bin/bash 
#Service Discovery Evaluation script

cd $HOME/Documents/workspace/A-Services-Clustering-Evaluation

# Services-Clustering
ant -Darg0=data/database/mydatabase.Prediction-7Cluster.db -Darg1=Output/output-7  run
ant -Darg0=data/database/mydatabase.Prediction-17Cluster.db -Darg1=Output/output-17  run
ant -Darg0=data/database/mydatabase.Prediction-38Cluster.db -Darg1=Output/output-38  run
ant -Darg0=data/database/mydatabase.Prediction-76Cluster.db -Darg1=Output/output-76  run
ant -Darg0=data/database/mydatabase.Prediction-116Cluster.db -Darg1=Output/output-116  run
ant -Darg0=data/database/mydatabase.Prediction-186Cluster.db -Darg1=Output/output-186  run


cd $HOME/Documents/workspace/A-Services-Classification-Evaluation

# Services-Classification
ant -Darg0=data/database/mydatabase.Prediction-10Observ.db -Darg1=Output/output-10  run
ant -Darg0=data/database/mydatabase.Prediction-30Observ.db -Darg1=Output/output-30  run
ant -Darg0=data/database/mydatabase.Prediction-60Observ.db -Darg1=Output/output-60  run
ant -Darg0=data/database/mydatabase.Prediction-100Observ.db -Darg1=Output/output-100  run
ant -Darg0=data/database/mydatabase.Prediction-200Observ.db -Darg1=Output/output-200  run


cd $HOME/Documents/workspace/A-Services-Prediction-Evaluation

# Services-Prediction
ant -Darg0=data/database/mydatabase.Prediction-8State.db -Darg1=Output/output-8  run
ant -Darg0=data/database/mydatabase.Prediction-17State.db -Darg1=Output/output-17  run
ant -Darg0=data/database/mydatabase.Prediction-38State.db -Darg1=Output/output-38  run
ant -Darg0=data/database/mydatabase.Prediction-67State.db -Darg1=Output/output-67  run
ant -Darg0=data/database/mydatabase.Prediction-84State.db -Darg1=Output/output-84  run
ant -Darg0=data/database/mydatabase.Prediction-168State.db -Darg1=Output/output-168  run