'use strict';

angular.module('polcoApp')
    .controller('GoodWordDetailController', function ($scope, $stateParams, GoodWord, BadWord) {
        $scope.goodWord = {};
        $scope.load = function (id) {
            GoodWord.get({id: id}, function(result) {
              $scope.goodWord = result;
            });
        };
        $scope.load($stateParams.id);
    });
