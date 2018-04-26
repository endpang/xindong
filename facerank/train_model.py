# 模型训练脚本
import numpy as np
from sklearn import decomposition
from sklearn.ensemble import RandomForestRegressor
from sklearn.externals import joblib


# 特征和对应的分数路径
features_path = './data/all.txt'
ratings_path = './data/rate.txt'

# 载入数据
# 共500组数据
# 其中前480组数据作为训练集，后20组数据作为测试集
features = np.loadtxt(features_path, delimiter=',')
features_train = features[0: -20]
features_test = features[-20: ]
ratings = np.loadtxt(ratings_path, delimiter=',')
ratings_train = ratings[0: -20]
ratings_test = ratings[-20: ]

# 训练模型
# 这里用PCA算法对特征进行了压缩和降维。
# 降维之后特征变成了20维，也就是说特征一共有500行，每行是一个人的特征向量，每个特征向量有20个元素。
# 用随机森林训练模型
pca = decomposition.PCA(n_components=20)
pca.fit(features_train)
features_train = pca.transform(features_train)
features_test = pca.transform(features_test)
regr = RandomForestRegressor(n_estimators=50, max_depth=None, min_samples_split=10, random_state=0)
regr = regr.fit(features_train, ratings_train)
joblib.dump(regr, './model/face_rating_new.pkl', compress=1)

# 训练完之后提示训练结束
print('Generate Model Successfully!')