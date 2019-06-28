from utils import detector_utils as detector_utils
from keras.models import model_from_json
import matplotlib.pyplot as plt
import numpy as np
import tensorflow as tf
import cv2
from DatasetLoader import DatasetLoader
from PIL import ImageOps

loader = DatasetLoader()
loader.load_data()

classes = loader.get_classes()

# Carregando arquitetura da rede
with open('model/model_architecture.json', 'r') as f:
    model = model_from_json(f.read())

# Carregando os pesos do modelo
model.load_weights('model/model_weights.h5')

graph = tf.get_default_graph()

detection_graph, sess = detector_utils.load_inference_graph()

# Score threshold for a bounding box be considerated as a hand
SCORE_THRESHOLD = 0.2
# Max number of hands we want to detect/track
NUM_HANDS_DETECT = 1
    
# Read image from webcam given some width and heigth
def readWebcamImage(width=320, height=180):
    # CV2 object to capture images from videos
    # with param 0 it captures the webcam image
    cam = cv2.VideoCapture(0)
    # Set the width and height of the image
    cam.set(cv2.CAP_PROP_FRAME_WIDTH, width)
    cam.set(cv2.CAP_PROP_FRAME_HEIGHT, height)
    # Read the image
    ret, frame = cam.read()
    # Release the webcam
    cam.release()
    
    # Convert image from BGR format to RGB
    image = cv2.cvtColor(frame,cv2.COLOR_BGR2RGB)
    
    return image

def realizarPredicao(image):
    global graph
    with graph.as_default():
        # Lê imagem da webcam
        HEIGHT = image.shape[0]
        WIDTH = image.shape[1]

        # Detecta as mãos na imagem
        boxes, scores = detector_utils.detect_objects(image, detection_graph, sess)

        # Pega as coorenadas da mão detectada
        (left, right, top, bottom) = (boxes[0][1] * WIDTH, boxes[0][3] * WIDTH,
                                      boxes[0][0] * HEIGHT, boxes[0][2] * HEIGHT)
        (left, right, top, bottom) = (int(left), int(right), int(top), int(bottom))

        # Corta a mão da imagem
        crop_img = image[top:bottom, left:right]
        # Desenha um retangulo na mão detectada na imagem
        #detector_utils.draw_box_on_image(1, SCORE_THRESHOLD, scores, boxes, WIDTH, HEIGHT, image)

        resized_image = cv2.resize(crop_img, (50, 50))
        img_gray = cv2.cvtColor(resized_image, cv2.COLOR_RGB2GRAY)
        im_flipped = cv2.flip(img_gray, 1)
        #plt.imshow(im_flipped, cmap='gray', vmin=0, vmax=255)
        #plt.show()
        im_flipped = im_flipped.reshape((1,50,50,1))
        y_predicted = model.predict(im_flipped)
        #y_predicted = np.argmax(y_predicted, axis=1)[0]
        idxPred = (-y_predicted).argsort()[:3]
        idxPred = idxPred[:, :3].squeeze()
        #return y_predicted
        return [classes[i] for i in idxPred]

#image = readWebcamImage()

#classesPredicted = realizarPredicao(image)

#cv2.rectangle(image_np, p1, p2, (77, 255, 9), 3, 1)

#detector_utils.draw_box_on_image(1, SCORE_THRESHOLD, scores, boxes, WIDTH, HEIGHT, image)

