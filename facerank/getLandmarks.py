# 人脸关键点提取脚本
import cv2
import dlib
import numpy


# 模型路径
PREDICTOR_PATH = './model/shape_predictor_68_face_landmarks.dat'
# 使用dlib自带的frontal_face_detector作为人脸提取器
detector = dlib.get_frontal_face_detector()
# 使用官方提供的模型构建特征提取器
predictor = dlib.shape_predictor(PREDICTOR_PATH)
face_img = cv2.imread("test_img/4.jpg")
# 使用detector进行人脸检测，rects为返回的结果
rects = detector(face_img, 1)
# 如果检测到人脸
if len(rects) >= 1:
	print("{} faces detected".format(len(rects)))
else:
	print('No faces detected')
	exit()
with open('./results/landmarks.txt', 'w') as f:
	f.truncate()
	for faces in range(len(rects)):
		# 使用predictor进行人脸关键点识别
		print(rects[faces])
		landmarks = numpy.matrix([[p.x, p.y] for p in predictor(face_img, rects[faces]).parts()])
		face_img = face_img.copy()
		# 使用enumerate函数遍历序列中的元素以及它们的下标
		for idx, point in enumerate(landmarks):
			pos = (point[0, 0], point[0, 1])
			f.write(str(point[0, 0]))
			f.write(',')
			f.write(str(point[0, 1]))
			f.write(',')
		f.write('\n')
	f.close()
# 成功后提示
print('Get landmarks successfully')