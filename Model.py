from keras.models import Sequential
from keras.layers import Conv2D, MaxPooling2D, Flatten, Dense, Dropout
from keras.callbacks import EarlyStopping, ReduceLROnPlateau
from keras.optimizers import Nadam
from DatasetLoader import DatasetLoader
import numpy as np
from sklearn.metrics import classification_report, confusion_matrix

loader = DatasetLoader()
loader.load_data()
loader.show_sample_image()

x_training, x_validation, y_training, y_validation = loader.get_dataset()

NUM_CLASSES = loader.get_number_classes()
INPUT_SHAPE = loader.get_dataset_shape()

def convNet():
    # Declara função de otimização da rede
    #optimizer = Nadam(lr=0.003, schedule_decay=0.05)
    optimizer = Nadam(lr=0.003)
    # Cria um modelo sequencial
    model = Sequential()
    
    model.add(Conv2D(filters=16, kernel_size=(5, 5), input_shape=INPUT_SHAPE, activation = 'relu', name="conv1"))
    model.add(MaxPooling2D(pool_size = (2, 2), name="maxPool1"))
    
    model.add(Conv2D(filters=32, kernel_size=(5, 5), activation = 'relu', name="conv2"))
    model.add(MaxPooling2D(pool_size=(2, 2), name="maxPool2"))
    
    model.add(Conv2D(filters=48, kernel_size=(4, 4), activation = 'relu', name="conv3"))
    model.add(MaxPooling2D(pool_size=(2, 2), name="maxPool3"))
    
    model.add(Flatten())
    
    # Camadas densas
    model.add(Dense(units=512, kernel_initializer="uniform", activation="relu", name="hidden1"))
    #model.add(Dropout(0.2))
    
    model.add(Dense(units = NUM_CLASSES, activation="softmax"))
    model.compile(optimizer = optimizer, loss="categorical_crossentropy", metrics = ["accuracy"])

    return model

#es = EarlyStopping(monitor = 'val_loss', min_delta = 1e-10, patience = 10, verbose = 1)
#rlr = ReduceLROnPlateau(monitor = 'val_loss', factor = 0.2, patience = 5, verbose = 1)

model = convNet()

training_history = model.fit(x_training, y_training, 
                             batch_size=32,
                             epochs=50,
                             verbose=1, validation_data=(x_validation, y_validation))

y_predicted = model.predict(x_validation)
y_predicted = np.argmax(y_predicted, axis=1)
y_true = np.argmax(y_validation, axis=1)

import seaborn as sn
import pandas as pd
import matplotlib.pyplot as plt

confusion_matrix = confusion_matrix(y_true, y_predicted)

df_cm = pd.DataFrame(confusion_matrix, index = loader.get_classes(),
                  columns = loader.get_classes())
plt.figure(figsize = (10,7))
sn.heatmap(df_cm, annot=True)
plt.show()

print('Classification Report')
print(classification_report(y_true, y_predicted, target_names=loader.classes))

# Salva pesos do modelo
model.save_weights("model/model_weights.h5")

# Salva a arquitetura do modelo
with open("model/model_architecture.json", "w") as f:
    f.write(model.to_json())

