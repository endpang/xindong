__author__ = 'pangzhiwei'

import cv2
import dlib
import numpy as np
import math
import itertools
from sklearn.externals import joblib
from sklearn import decomposition
import bottle
from bottle import request
import urllib.request
import json


def facialRatio(points):
	x1 = points[0]
	y1 = points[1]
	x2 = points[2]
	y2 = points[3]
	x3 = points[4]
	y3 = points[5]
	x4 = points[6]
	y4 = points[7]
	dist1 = math.sqrt((x1-x2)**2 + (y1-y2)**2)
	dist2 = math.sqrt((x3-x4)**2 + (y3-y4)**2)
	ratio = dist1/dist2
	return ratio


def generateFeatures(pointIndices1, pointIndices2, pointIndices3, pointIndices4, allLandmarkCoordinates):
	size = allLandmarkCoordinates.shape
	if len(size) > 1:
		allFeatures = np.zeros((size[0], len(pointIndices1)))
		for x in range(0, size[0]):
			landmarkCoordinates = allLandmarkCoordinates[x, :]
			ratios = []
			for i in range(0, len(pointIndices1)):
				x1 = landmarkCoordinates[2*(pointIndices1[i]-1)]
				y1 = landmarkCoordinates[2*pointIndices1[i] - 1]
				x2 = landmarkCoordinates[2*(pointIndices2[i]-1)]
				y2 = landmarkCoordinates[2*pointIndices2[i] - 1]
				x3 = landmarkCoordinates[2*(pointIndices3[i]-1)]
				y3 = landmarkCoordinates[2*pointIndices3[i] - 1]
				x4 = landmarkCoordinates[2*(pointIndices4[i]-1)]
				y4 = landmarkCoordinates[2*pointIndices4[i] - 1]
				points = [x1, y1, x2, y2, x3, y3, x4, y4]
				ratios.append(facialRatio(points))
			allFeatures[x, :] = np.asarray(ratios)
	else:
		allFeatures = np.zeros((1, len(pointIndices1)))
		landmarkCoordinates = allLandmarkCoordinates
		ratios = []
		for i in range(0, len(pointIndices1)):
			x1 = landmarkCoordinates[2*(pointIndices1[i]-1)]
			y1 = landmarkCoordinates[2*pointIndices1[i] - 1]
			x2 = landmarkCoordinates[2*(pointIndices2[i]-1)]
			y2 = landmarkCoordinates[2*pointIndices2[i] - 1]
			x3 = landmarkCoordinates[2*(pointIndices3[i]-1)]
			y3 = landmarkCoordinates[2*pointIndices3[i] - 1]
			x4 = landmarkCoordinates[2*(pointIndices4[i]-1)]
			y4 = landmarkCoordinates[2*pointIndices4[i] - 1]
			points = [x1, y1, x2, y2, x3, y3, x4, y4]
			ratios.append(facialRatio(points))
		allFeatures[0, :] = np.asarray(ratios)
	return allFeatures


def generateAllFeatures(allLandmarkCoordinates):
	a = [18, 22, 23, 27, 37, 40, 43, 46, 28, 32, 34, 36, 5, 9, 13, 49, 55, 52, 58]
	combinations = itertools.combinations(a, 4)
	i = 0
	pointIndices1 = []
	pointIndices2 = []
	pointIndices3 = []
	pointIndices4 = []
	for combination in combinations:
		pointIndices1.append(combination[0])
		pointIndices2.append(combination[1])
		pointIndices3.append(combination[2])
		pointIndices4.append(combination[3])
		i = i+1
		pointIndices1.append(combination[0])
		pointIndices2.append(combination[2])
		pointIndices3.append(combination[1])
		pointIndices4.append(combination[3])
		i = i+1
		pointIndices1.append(combination[0])
		pointIndices2.append(combination[3])
		pointIndices3.append(combination[1])
		pointIndices4.append(combination[2])
		i = i+1
	return generateFeatures(pointIndices1, pointIndices2, pointIndices3, pointIndices4, allLandmarkCoordinates)


def fetch_face_pic(face,predictor):
    rects = detector(face, 1)
    #str = ""
    #strs = ""
    arrs = []
    face_arr = []
    for faces in range(len(rects)):
        # 使用predictor进行人脸关键点识别
        #print(rects[faces])
        landmarks = np.matrix([[p.x, p.y] for p in predictor(face, rects[faces]).parts()])
        #face_img = face.copy()
        # 使用enumerate函数遍历序列中的元素以及它们的下标
        arr = []

        for idx, point in enumerate(landmarks):
            arr = np.append(arr,point[0,0])
            arr = np.append(arr,point[0,1])
            #strs += str(point[0, 0]) + ','  + str(point[0, 1]) + ','
            #pos = (point[0, 0], point[0, 1])
            #print(point)
            #f.write(str(point[0, 0]))
            #f.write(',')
            #f.write(str(point[0, 1]))
            #f.write(',')
            #f.write('\n')
        if len(arrs) == 0:
            arrs = [arr]
        else:
            arrs = np.concatenate((arrs,[arr]),axis=0)
        f = rects[faces]
        [x1,x2,y1,y2]=[f.left(),f.right(),f.top(),f.bottom()]
        a = [[x1,x2,y1,y2]]
        if len(face_arr) == 0:
            face_arr = a
        else:
            face_arr = np.concatenate((face_arr,a) ,axis=0)
    return arrs,face_arr

def predict(my_features):
    predictions = []
    for i in range(len(my_features)):
        feature = my_features[i, :]
        feature_transfer = pca.transform(feature.reshape(1, -1))
        predictions.append(pre_model.predict(feature_transfer).tolist())
        print(i)
    '''
    if len(my_features.shape) > 1:
        for i in range(len(my_features)):
            feature = my_features[i, :]
            feature_transfer = pca.transform(feature.reshape(1, -1))
            predictions.append(pre_model.predict(feature_transfer))
        print('照片中的人颜值得分依次为(满分为5分)：')
        k = 1
        for pre in predictions:
            print('第%d个人：' % k, end='')
            print(str(pre)+'分')
            k += 1
    else:
        feature = my_features
        feature_transfer = pca.transform(feature.reshape(1, -1))
        predictions.append(pre_model.predict(feature_transfer))
        print('照片中的人颜值得分为(满分为5分)：')
        k = 1
        for pre in predictions:
            print(str(pre)+'分')
            k += 1
    '''
    return predictions

PREDICTOR_PATH = './model/shape_predictor_68_face_landmarks.dat'
detector = dlib.get_frontal_face_detector()
# 使用官方提供的模型构建特征提取器
predictor = dlib.shape_predictor(PREDICTOR_PATH)
pre_model = joblib.load('./model/face_rating.pkl')
features = np.loadtxt('./data/features_ALL.txt', delimiter=',')
pca = decomposition.PCA(n_components=20)
pca.fit(features)


@bottle.route('/find', method='GET')
def do_find():
    w = request.query.get("url")
    #print(w)
    resp = urllib.request.urlopen(w)
    image = np.asarray(bytearray(resp.read()),dtype="uint8")
    image = cv2.imdecode(image,cv2.IMREAD_COLOR)
    arrs,faces = fetch_face_pic(image,predictor)
    print(arrs)
    my_features = generateAllFeatures(arrs)
    if len(my_features.shape) > 1:
        predictions = predict(my_features,)
        print(faces)
        print(predictions)
        # print(type(predictions))
        result =[
            faces.tolist(),predictions
        ]
    #print(image)
    print(faces)
    return json.dumps(result)


bottle.run(host='0.0.0.0', port=8888)