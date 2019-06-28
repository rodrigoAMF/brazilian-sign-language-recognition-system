from flask import Flask
from flask import request
from flask_cors import CORS
import json
import numpy as np
import cv2
import base64
import io
from detect import *
from PIL import Image

app = Flask(__name__)
CORS(app)


@app.route('/', methods=['GET', 'POST'])
def index():
    # read image file string data
    #filestr = request.files['file'].read()
    # convert string data to numpy array
    #npimg = np.fromstring(filestr, np.uint8)
    # convert numpy array to image
    #img = cv2.imdecode(npimg, cv2.IMREAD_COLOR)
    #jsonObj['status'] = 200
    #tamanho_tabuleiro = request.args.get('tamanho_tabuleiro')

    #imgdata = base64.b64decode(request.form['image'])
    #filename = 'some_image.jpg'  # I assume you have a way of picking unique filenames
    #with open(filename, 'wb') as f:
    #    f.write(imgdata)
    jsonObj = {}
    imagem = stringToRGB(request.form['image'])
    classes = realizarPredicao(imagem)
    jsonObj['classes'] = classes
    cv2.imwrite('imagem.png', imagem)

    return json.dumps(jsonObj)

def stringToRGB(base64_string):
    imgdata = base64.b64decode(str(base64_string))
    image = Image.open(io.BytesIO(imgdata))
    return cv2.cvtColor(np.array(image), cv2.COLOR_BGR2RGB)

if __name__ == "__main__":
    #app.run()
    app.run(host='192.168.43.110')
