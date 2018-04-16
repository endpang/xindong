# 颜值预测脚本
from sklearn.externals import joblib
import numpy as np
from sklearn import decomposition


pre_model = joblib.load('./model/face_rating.pkl')
features = np.loadtxt('./data/features_ALL.txt', delimiter=',')
my_features = np.loadtxt('./results/my_features.txt', delimiter=',')
pca = decomposition.PCA(n_components=20)
pca.fit(features)
predictions = []
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