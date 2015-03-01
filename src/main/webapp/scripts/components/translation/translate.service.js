'use strict';

angular.module('polcoApp')
    .factory('Translation',  ['$http', function($http) {

        var urlBase = '/api/translation';
        var dataFactory = {};

        dataFactory.getTranslation = function (textToTranslate) {
            return $http.post(urlBase, textToTranslate);
        };

        return dataFactory;
    }]);
