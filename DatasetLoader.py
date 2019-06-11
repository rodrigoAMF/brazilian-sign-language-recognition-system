import numpy as np
import matplotlib.image as mpimg  # for reading in images
import matplotlib.pyplot as plt   #for plot images
import cv2 # computer vision library
from sklearn.model_selection import train_test_split

from os import listdir, walk
from os.path import isfile, join


class DatasetLoader:
    def __init__(self):
        self.dataset_path = "dataset/"
        self.classes = [x for x in walk("dataset/Fold1/")][0][1]
        self.classes = [d for d in self.classes if len(d) == 1]
        self.classes_map = {key: value for (value, key) in enumerate(self.classes)}
        self.test_size = 0.2
        self.x = None
        self.y = None

    # Load data to array
    def load_data(self):
        x = []
        y = []
        for index in range(1, 7):
            base_path = self.dataset_path + "Fold" + str(index) + "/"
            for class_name in self.classes:
                files_path = base_path + class_name + "/"
                files = [f for f in listdir(files_path) if isfile(join(files_path, f)) and f[0] != 'c']
                for file in files:
                    image = mpimg.imread(files_path + file)
                    gray_image = cv2.cvtColor(image, cv2.COLOR_RGB2GRAY)
                    x.append(gray_image)
                    label_for_image = np.zeros(len(self.classes), dtype=int)
                    label_for_image[self.classes_map[class_name]] = 1
                    y.append(label_for_image)

        x, y = np.array(x), np.array(y)
        x = x.reshape((x.shape[0], 50, 50, 1))

        self.x, self.y = x, y

    def show_sample_image(self):
        if self.x is not None:
            index = np.random.randint(0, self.x.shape[0])
            plt.imshow(self.x[index].reshape((50, 50)), cmap='gray')
            plt.show()

    def get_dataset(self):
        return train_test_split(self.x, self.y, test_size=self.test_size)

    def get_dataset_shape(self):
        return self.x[0].shape

    def get_classes(self):
        return self.classes

    def get_number_classes(self):
        return len(self.classes)
