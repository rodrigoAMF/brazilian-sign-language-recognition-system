from utils import detector_utils as detector_utils
import matplotlib.pyplot as plt
import cv2
import tensorflow as tf

detection_graph, sess = detector_utils.load_inference_graph()

WIDTH = 320
HEIGHT = 180
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

image = readWebcamImage(WIDTH, HEIGHT)

boxes, scores = detector_utils.detect_objects(image, detection_graph, sess)

detector_utils.draw_box_on_image(1, SCORE_THRESHOLD, scores, boxes, WIDTH, HEIGHT, image)

plt.imshow(image)